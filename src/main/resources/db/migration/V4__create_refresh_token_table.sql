CREATE TABLE refresh_token (
                               id BIGSERIAL PRIMARY KEY,
                               token VARCHAR(255) NOT NULL UNIQUE,
                               expiry_date TIMESTAMP NOT NULL,
                               user_id BIGINT UNIQUE,

                               CONSTRAINT fk_user
                                   FOREIGN KEY(user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);