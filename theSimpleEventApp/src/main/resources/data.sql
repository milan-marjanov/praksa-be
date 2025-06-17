
INSERT INTO users (id, first_name, last_name, email, password, profile_picture_url, role)
VALUES (3, 'Petar', 'Petrović', 'petar@example.com', 'encoded-password', null, 'USER');

INSERT INTO event (id, title, description, user_id, chat_id)
VALUES (1, 'Rođendanska zabava', 'Proslava rođendana sa prijateljima', 3, null);

INSERT INTO time_option (id, max_capacity, start_time, end_time, deadline, created_at, event_id)
VALUES (
           1,
           10,
           '2025-07-01T18:00:00',
           '2025-07-01T21:00:00',
           '2025-06-25T23:59:59',
           '2025-06-17T10:00:00',
           1
       );

INSERT INTO time_option (id, max_capacity, start_time, end_time, deadline, created_at, event_id)
VALUES (
           2,
           10,
           '2025-07-01T18:00:00',
           '2025-07-01T21:00:00',
           '2025-06-25T23:59:59',
           '2025-06-17T10:00:00',
           1
       );


INSERT INTO restaurant_option (id, name, menu_image_url, restaurant_url, event_id)
VALUES (
           1,
           'Restoran Kalemegdan',
           NULL,
           'https://kalemegdan.rs',
           1
       );