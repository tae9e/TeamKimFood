CREATE TABLE comments (
                          comment_date TIMESTAMP(6),
                          comment_id BIGINT AUTO_INCREMENT,
                          correction_date TIMESTAMP(6),
                          member_id BIGINT,
                          recipe_id BIGINT,
                          content VARCHAR(255),
                          PRIMARY KEY (comment_id),
                          FOREIGN KEY (member_id) REFERENCES member(member_id),
                          FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
);

CREATE TABLE foodfile (
                          created_at TIMESTAMP(6),
                          foodfile_id BIGINT AUTO_INCREMENT,
                          member_id BIGINT,
                          file_name VARCHAR(255),
                          file_type VARCHAR(255),
                          PRIMARY KEY (foodfile_id),
                          FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE foodimg (
                         foodfile_id BIGINT,
                         foodimg_id BIGINT NOT NULL AUTO_INCREMENT,
                         magazines_id BIGINT,
                         recipe_id BIGINT,
                         PRIMARY KEY (foodimg_id),
                         FOREIGN KEY (foodfile_id) REFERENCES foodfile(foodfile_id),
                         FOREIGN KEY (magazines_id) REFERENCES magazines(magazines_id),
                         FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
);

CREATE TABLE magazines (
                           correction_date TIMESTAMP(6),
                           magazines_id BIGINT AUTO_INCREMENT,
                           member_id BIGINT,
                           write_date TIMESTAMP(6),
                           content VARCHAR(255),
                           img_name VARCHAR(255),
                           title VARCHAR(255),
                           PRIMARY KEY (magazines_id),
                           FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE member (
                        grade INTEGER DEFAULT 0 NOT NULL,
                        joined_date TIMESTAMP(6),
                        member_id BIGINT AUTO_INCREMENT,
                        email VARCHAR(255) UNIQUE,
                        member_role VARCHAR(255) CHECK (member_role IN ('USER', 'ADMIN')),
                        name VARCHAR(255),
                        nickname VARCHAR(255),
                        PASSWORD VARCHAR(255),
                        phone_number VARCHAR(255),
                        PRIMARY KEY (member_id)
);

CREATE TABLE member_preference (
                                   member_id BIGINT UNIQUE,
                                   mpreference_id BIGINT NOT NULL AUTO_INCREMENT,
                                   food_nation_type VARCHAR(255),
                                   food_stuff VARCHAR(255),
                                   situation VARCHAR(255),
                                   PRIMARY KEY (mpreference_id),
                                   FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE ranks (
                       member_id BIGINT UNIQUE,
                       rank_id BIGINT AUTO_INCREMENT,
                       recipe_id BIGINT UNIQUE,
                       rank_search_status VARCHAR(255) CHECK (rank_search_status IN ('MEMBER', 'RECIPE')),
                       PRIMARY KEY (rank_id),
                       FOREIGN KEY (member_id) REFERENCES member(member_id),
                       FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
);

CREATE TABLE recipe (
                        view_count INTEGER DEFAULT 0 NOT NULL,
                        correction_date TIMESTAMP(6),
                        member_id BIGINT,
                        recipe_id BIGINT AUTO_INCREMENT,
                        score BIGINT DEFAULT 0 NOT NULL,
                        write_date TIMESTAMP(6),
                        content VARCHAR(255),
                        title VARCHAR(255),
                        PRIMARY KEY (recipe_id),
                        FOREIGN KEY (member_id) REFERENCES member(member_id)
);

CREATE TABLE recipe_detail (
                               recipe_dtl_id BIGINT AUTO_INCREMENT,
                               recipe_id BIGINT,
                               dosage VARCHAR(255),
                               ingredients VARCHAR(255),
                               PRIMARY KEY (recipe_dtl_id),
                               FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
);

CREATE TABLE recipe_category (
                                 mpreference_id BIGINT NOT NULL,
                                 recipe_id BIGINT UNIQUE,
                                 food_nation_type VARCHAR(255),
                                 food_stuff VARCHAR(255),
                                 situation VARCHAR(255),
                                 PRIMARY KEY (mpreference_id),
                                 FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
);