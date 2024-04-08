package com.hits.common.Core;

public class Consts {
    public static final String UPLOAD_FILE = "/api/file/upload";
    public static final String DOWNLOAD_FILE = "/api/file/download";
    public static final String DELETE_FILE = "/api/file/delete";
    public static final String GET_FILES = "/api/file/get";
    public static final String GET_FILE_INFO = "/api/file/info";
    public static final String REFRESH_TOKEN = "/api/account/refreshToken";
    public static final String REGISTER_USER = "/api/account/register";
    public static final String LOGIN_USER = "/api/account/login";
    public static final String LOGOUT_USER = "/api/account/logout";
    public static final String GET_PROFILE = "/api/account";
    public static final String VERIFY_USER = "/api/account/verify";
    public static final String VALIDATE_TOKEN = "/api/token/validate";
    public static final String GET_USER = "/api/account/user";
    public static final String BAN_USER = "/api/account/admin/ban";
    public static final String CREATE_USER = "/api/account/admin/create/user";
    public static final String EDIT_USER = "/api/account/admin/edit/user";
    public static final String GIVE_MODERATOR = "/api/account/admin/give/moderator";
    public static final String DELETE_MODERATOR = "/api/account/admin/delete/moderator";
    public static final String GIVE_CATEGORY = "/api/account/admin/give/category";
    public static final String ADD_TO_FAVORITE = "/api/account/favorite/add";
    public static final String DELETE_FROM_FAVORITE = "/api/account/favorite/delete";
    public static final String GET_FAVORITE = "/api/account/favorite";
    public static final String EUREKA = "/eureka";
    public static final String CREATE_CATEGORY = "/api/forum/category";
    public static final String GET_CATEGORIES = "/api/forum/category/get";
    public static final String CREATE_THEME = "/api/forum/category/theme";
    public static final String GET_THEMES = "/api/forum/category/theme/get";
    public static final String GET_THEMES_BY_ID = "/api/forum/category/themes";
    public static final String SEND_MESSAGE = "/api/forum/theme/message";
    public static final String EDIT_CATEGORY = "/api/forum/category/edit";
    public static final String EDIT_THEME = "/api/forum/category/theme/edit";
    public static final String EDIT_MESSAGE = "/api/forum/theme/message/edit";
    public static final String DELETE_CATEGORY = "/api/forum/category/delete";
    public static final String DELETE_THEME = "/api/forum/category/theme/delete";
    public static final String DELETE_ATTACHMENT = "/api/forum/message/attachment/delete";
    public static final String CHECK_THEME = "/api/forum/category/theme";
    public static final String CHECK_CATEGORY = "/api/forum/category";
    public static final String ARCHIVE_THEME = "/api/forum/category/theme/archive";
    public static final String DELETE_MESSAGE = "/api/forum/theme/message/delete";
    public static final String GET_MESSAGES = "/api/forum/theme/{themeId}/message/get";
    public static final String GET_MESSAGES_WITH_FILTERS = "/api/forum/message/get";
    public static final String GET_CATEGORIES_WITH_SUBSTRING = "/api/forum/categories";
    public static final String GET_THEMES_WITH_SUBSTRING = "/api/forum/themes";
    public static final String GET_MESSAGES_WITH_SUBSTRING = "/api/forum/messages";
    public static final String ADMIN = "ADMIN";
    public static final String MODERATOR = "MODERATOR";
}
