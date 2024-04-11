package com.hits.user.Core.User.Service;

import com.hits.common.Core.Response.Response;
import com.hits.common.Core.Response.TokenResponse;
import com.hits.common.Core.Theme.DTO.ThemeDto;
import com.hits.common.Core.User.DTO.UserDto;
import com.hits.common.Exceptions.BadRequestException;
import com.hits.common.Exceptions.NotFoundException;
import com.hits.common.Exceptions.UnknownException;
import com.hits.security.Rest.Client.ForumAppClient;
import com.hits.user.Core.RefreshToken.Entity.RefreshToken;
import com.hits.user.Core.RefreshToken.Service.RefreshTokenService;
import com.hits.user.Core.User.Entity.User;
import com.hits.user.Core.User.Mapper.UserMapper;
import com.hits.user.Core.User.Repository.UserRepository;
import com.hits.user.Core.Utils.JwtTokenUtils;
import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final RefreshTokenService refreshTokenService;
    private final ForumAppClient forumAppClient;

    @Transactional
    @Override
    public User loadUserByUsername(String login) throws UsernameNotFoundException{
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с указанным логином не найден"));
    }


    public UserDto getUserFromLogin(String login) throws NotFoundException{
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя с логином=%s не существует", login)));

        return UserMapper.userToUserDto(user);
    }

    @Transactional
    public ResponseEntity<?> addThemeToFavorite(UserDto userDto, UUID themeId) throws NotFoundException{
        User user = userRepository.findByLogin(userDto.getLogin()).get();

        ResponseEntity<?> checkTheme;
        try {
             checkTheme = forumAppClient.checkTheme(themeId);
        }
        catch (FeignException.NotFound e){
            throw new NotFoundException(String.format("Темы с id=%s не существует", themeId));
        }

        if (checkTheme != null && checkTheme.getStatusCode() == HttpStatus.OK){
            List<UUID> favoriteThemes = user.getFavoriteThemes();
            if (favoriteThemes.contains(themeId)){
                throw new BadRequestException(String.format("Тема с id=%s уже находится в избранном пользователя", themeId));
            }

            favoriteThemes.add(themeId);
            userRepository.saveAndFlush(user);

            return new ResponseEntity<>(new Response(HttpStatus.OK.value(),
                    "Пользователь успешно добавил тему в избранное"), HttpStatus.OK);
        }

        throw new UnknownException();
    }

    @Transactional
    public ResponseEntity<?> deleteThemeFromFavorite(UserDto userDto, UUID themeId) throws NotFoundException{
        User user = userRepository.findByLogin(userDto.getLogin()).get();

        if (user.getFavoriteThemes().contains(themeId)){
            user.getFavoriteThemes().remove(themeId);

            userRepository.saveAndFlush(user);

            return new ResponseEntity<>(new Response(HttpStatus.OK.value(),
                    "Пользователь успешно удалил тему из избранного"), HttpStatus.OK);
        }
        else{
            throw new NotFoundException(String.format("Темы с id=%s не существует", themeId));
        }
    }

    public ResponseEntity<List<ThemeDto>> getFavoriteThemes(UserDto userDto){
        User user = userRepository.findByLogin(userDto.getLogin()).get();

        List<ThemeDto> favoriteThemes;

        try {
            favoriteThemes = forumAppClient.getThemesById(user.getFavoriteThemes()).getBody();
        }
        catch (FeignException.InternalServerError e) {
            throw new UnknownException();
        }

        return ResponseEntity.ok(favoriteThemes);
    }

    @Transactional
    public ResponseEntity<?> verifyUser(UUID userId, String code) throws NotFoundException, BadRequestException{
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя с id=%s не существует", userId)));

        if (!user.getIsConfirmed()){
            if (code.equals(user.getVerificationCode())) {
                user.setIsConfirmed(true);
                user.setVerificationCode(null);
                userRepository.saveAndFlush(user);

                String token = jwtTokenUtils.generateToken(user);
                jwtTokenUtils.saveToken(jwtTokenUtils.getIdFromToken(token), "Valid");
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

                return ResponseEntity.ok(new TokenResponse(token, refreshToken.getToken()));
            }
            throw new BadRequestException("Неверный код подтверждения аккаунта");
        }
        else{
            return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(),
                    "Почта пользователя уже подтверждена"), HttpStatus.BAD_REQUEST);
        }
    }

    public Boolean validateToken(String token){
        try {
            return jwtTokenUtils.validateToken(token);
        }
        catch (ExpiredJwtException | FeignException.Unauthorized e){
            return false;
        }
    }
}