-- liquibase formatted sql

-- changeset Vu:1744818566828-1
CREATE TABLE equipments
(
    equipment_name VARCHAR(50) NOT NULL,
    `description`  TEXT NULL,
    CONSTRAINT pk_equipments PRIMARY KEY (equipment_name)
);

-- changeset Vu:1744818566828-2
CREATE TABLE `groups`
(
    group_name   VARCHAR(50) NOT NULL,
    location     VARCHAR(100) NULL,
    division     VARCHAR(50) NULL,
    created_date datetime NULL,
    CONSTRAINT pk_groups PRIMARY KEY (group_name)
);

-- changeset Vu:1744818566828-3
CREATE TABLE invalidated_token
(
    id          VARCHAR(255) NOT NULL,
    expiry_time datetime NULL,
    CONSTRAINT pk_invalidated_token PRIMARY KEY (id)
);

-- changeset Vu:1744818566828-4
CREATE TABLE notifications
(
    notification_id BIGINT AUTO_INCREMENT NOT NULL,
    content         TEXT NULL,
    type            VARCHAR(255) NOT NULL,
    has_read        BIT(1)       NOT NULL,
    user_id         BIGINT NULL,
    created_at      datetime NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (notification_id)
);

-- changeset Vu:1744818566828-5
CREATE TABLE password_reset_otps
(
    otp         VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NULL,
    expiry_date datetime NULL,
    CONSTRAINT pk_password_reset_otps PRIMARY KEY (otp)
);

-- changeset Vu:1744818566828-6
CREATE TABLE permissions
(
    permission_name VARCHAR(50) NOT NULL,
    `description`   TEXT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (permission_name)
);

-- changeset Vu:1744818566828-7
CREATE TABLE positions
(
    position_name VARCHAR(75) NOT NULL,
    `description` TEXT NULL,
    CONSTRAINT pk_positions PRIMARY KEY (position_name)
);

-- changeset Vu:1744818566828-8
CREATE TABLE role_permissions
(
    permission_name VARCHAR(50) NOT NULL,
    role_name       VARCHAR(50) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (permission_name, role_name)
);

-- changeset Vu:1744818566828-9
CREATE TABLE roles
(
    role_name     VARCHAR(50) NOT NULL,
    `description` TEXT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (role_name)
);

-- changeset Vu:1744818566828-10
CREATE TABLE room_bookings
(
    booking_id    BIGINT AUTO_INCREMENT NOT NULL,
    room_id       BIGINT      NOT NULL,
    booked_by     BIGINT      NOT NULL,
    start_time    datetime    NOT NULL,
    end_time      datetime    NOT NULL,
    purpose       VARCHAR(20) NOT NULL,
    status        VARCHAR(20) NOT NULL,
    `description` TEXT NULL,
    created_at    datetime NULL,
    CONSTRAINT pk_roombookings PRIMARY KEY (booking_id)
);

-- changeset Vu:1744818566828-11
CREATE TABLE room_equipment
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    room_id        BIGINT      NOT NULL,
    equipment_name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_room_equipment PRIMARY KEY (id)
);

-- changeset Vu:1744818566828-12
CREATE TABLE rooms
(
    room_id   BIGINT AUTO_INCREMENT NOT NULL,
    room_name VARCHAR(100) NOT NULL,
    location  VARCHAR(255) NOT NULL,
    capacity  INT          NOT NULL,
    available BIT(1)       NOT NULL,
    note      TEXT NULL,
    image_url VARCHAR(255) NULL,
    CONSTRAINT pk_rooms PRIMARY KEY (room_id)
);

-- changeset Vu:1744818566828-13
CREATE TABLE user_roles
(
    role_name VARCHAR(50) NOT NULL,
    user_id   BIGINT      NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_name, user_id)
);

-- changeset Vu:1744818566828-14
CREATE TABLE users
(
    user_id      BIGINT AUTO_INCREMENT NOT NULL,
    user_name    VARCHAR(50)  NOT NULL,
    full_name    VARCHAR(100) NULL,
    department   VARCHAR(50) NULL,
    email        VARCHAR(50) NULL,
    phone_number VARCHAR(20) NULL,
    position_id  VARCHAR(75) NULL,
    password     VARCHAR(255) NOT NULL,
    enabled      BIT(1)       NOT NULL,
    group_id     VARCHAR(50) NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

-- changeset Vu:1744818566828-15
ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

-- changeset Vu:1744818566828-16
ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (user_name);

-- changeset Vu:1744818566828-17
ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

-- changeset Vu:1744818566828-18
ALTER TABLE room_bookings
    ADD CONSTRAINT FK_ROOMBOOKINGS_ON_BOOKEDBY FOREIGN KEY (booked_by) REFERENCES users (user_id);

-- changeset Vu:1744818566828-19
ALTER TABLE room_bookings
    ADD CONSTRAINT FK_ROOMBOOKINGS_ON_ROOMID FOREIGN KEY (room_id) REFERENCES rooms (room_id);

-- changeset Vu:1744818566828-20
ALTER TABLE room_equipment
    ADD CONSTRAINT FK_ROOM_EQUIPMENT_ON_EQUIPMENTNAME FOREIGN KEY (equipment_name) REFERENCES equipments (equipment_name) ON DELETE CASCADE;

-- changeset Vu:1744818566828-21
ALTER TABLE room_equipment
    ADD CONSTRAINT FK_ROOM_EQUIPMENT_ON_ROOMID FOREIGN KEY (room_id) REFERENCES rooms (room_id) ON DELETE CASCADE;

-- changeset Vu:1744818566828-22
ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_GROUPID FOREIGN KEY (group_id) REFERENCES `groups` (group_name) ON DELETE SET NULL;

-- changeset Vu:1744818566828-23
ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_POSITIONID FOREIGN KEY (position_id) REFERENCES positions (position_name) ON DELETE SET NULL;

-- changeset Vu:1744818566828-24
ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permission_name) REFERENCES permissions (permission_name);

-- changeset Vu:1744818566828-25
ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES roles (role_name);

-- changeset Vu:1744818566828-26
ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_name) REFERENCES roles (role_name);

-- changeset Vu:1744818566828-27
ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (user_id);

