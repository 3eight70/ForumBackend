package com.hits.notification.Core.Notification.Handler;

import com.hits.common.Core.Notification.Enum.NotificationChannel;
import com.hits.common.Core.User.DTO.UserNotificationDto;
import com.hits.notification.Core.Notification.Entity.Notification;
import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Getter
@Setter
public abstract class NotificationHandler {
    private NotificationHandler next;

    public abstract void handle(Notification notification, UserNotificationDto userNotificationDto, List<NotificationChannel> channels)
            throws MessagingException, UnsupportedEncodingException;
}
