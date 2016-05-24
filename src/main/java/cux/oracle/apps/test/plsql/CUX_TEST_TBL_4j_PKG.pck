CREATE OR REPLACE PACKAGE cux_test_tbl_4j_pkg AS

  /*=====================================
   ** PROCEDURE:   insert_or_update_row()
  **=====================================*/
  PROCEDURE insert_or_update_row(p_created_by          IN NUMBER,
                                 p_attribute1          IN VARCHAR2 DEFAULT NULL,
                                 x_row_version_number  IN OUT NUMBER,
                                 p_attirbute_category  IN VARCHAR2 DEFAULT NULL,
                                 p_my_name             IN VARCHAR2 DEFAULT NULL,
                                 p_last_updated_by     IN NUMBER,
                                 x_my_id               IN OUT NUMBER,
                                 p_last_update_date    IN DATE,
                                 p_creation_date       IN DATE,
                                 p_my_other_attribtues IN VARCHAR2 DEFAULT NULL,
                                 p_last_update_login   IN NUMBER DEFAULT NULL);

  /*=====================================
   **  PROCEDURE lock_row(
  **=====================================*/
  PROCEDURE lock_row(p_row_version_number IN NUMBER,
                     p_my_id              IN NUMBER);

  /*=====================================
   **  PROCEDURE delete_row(
  **=====================================*/
  PROCEDURE delete_row(p_my_id IN NUMBER);

END cux_test_tbl_4j_pkg;
/
CREATE OR REPLACE PACKAGE BODY cux_test_tbl_4j_pkg AS

  /*=====================================
   ** PROCEDURE:   insert_or_update_row()
  **=====================================*/
  PROCEDURE insert_or_update_row(p_created_by          IN NUMBER,
                                 p_attribute1          IN VARCHAR2 DEFAULT NULL,
                                 x_row_version_number  IN OUT NUMBER,
                                 p_attirbute_category  IN VARCHAR2 DEFAULT NULL,
                                 p_my_name             IN VARCHAR2 DEFAULT NULL,
                                 p_last_updated_by     IN NUMBER,
                                 x_my_id               IN OUT NUMBER,
                                 p_last_update_date    IN DATE,
                                 p_creation_date       IN DATE,
                                 p_my_other_attribtues IN VARCHAR2 DEFAULT NULL,
                                 p_last_update_login   IN NUMBER DEFAULT NULL) IS
    CURSOR c IS
      SELECT 1
        FROM cux_test_tbl
       WHERE my_id = x_my_id;
    l_insert_flag VARCHAR2(1) := 'N';
    l_check       NUMBER;
  BEGIN
    OPEN c;
    FETCH c
      INTO l_check;
    IF (c%NOTFOUND) THEN
      l_insert_flag := 'Y';
    END IF;
    CLOSE c;
    IF x_my_id IS NULL THEN
      l_insert_flag        := 'Y';
      x_row_version_number := 1;
      SELECT cux_test_tbl_s.nextval
        INTO x_my_id
        FROM dual;
    END IF;
    IF l_insert_flag = 'Y' THEN
      INSERT INTO cux_test_tbl
        (created_by,
         attribute1,
         row_version_number,
         attirbute_category,
         my_name,
         last_updated_by,
         my_id,
         last_update_date,
         creation_date,
         my_other_attribtues,
         last_update_login)
      VALUES
        (p_created_by,
         p_attribute1,
         x_row_version_number,
         p_attirbute_category,
         p_my_name,
         p_last_updated_by,
         x_my_id,
         p_last_update_date,
         p_creation_date,
         p_my_other_attribtues,
         p_last_update_login);
    ELSE
      lock_row(x_my_id, x_row_version_number);
      x_row_version_number := x_row_version_number + 1;
      UPDATE cux_test_tbl
         SET attribute1          = p_attribute1,
             row_version_number  = x_row_version_number,
             attirbute_category  = p_attirbute_category,
             my_name             = p_my_name,
             last_updated_by     = p_last_updated_by,
             my_id               = x_my_id,
             last_update_date    = p_last_update_date,
             my_other_attribtues = p_my_other_attribtues,
             last_update_login   = p_last_update_login
       WHERE my_id = x_my_id;
    END IF;
  END insert_or_update_row;

  /*=====================================
   **  PROCEDURE lock_row(
  **=====================================*/
  PROCEDURE lock_row(p_row_version_number NUMBER,
                     p_my_id              NUMBER) IS
    CURSOR c IS
      SELECT *
        FROM cux_test_tbl
       WHERE my_id = p_my_id
         FOR UPDATE OF my_id NOWAIT;
    rec c%ROWTYPE;
  BEGIN
    OPEN c;
    FETCH c
      INTO rec;
    IF (c%NOTFOUND) THEN
      CLOSE c;
      raise_application_error(-20003, '记录已被删除！');
    END IF;
    CLOSE c;
    IF ((rec.row_version_number <> p_row_version_number)) THEN
      raise_application_error(-20003, '记录已被其他用户更新，请重新打开查询已查看更改！');
    END IF;
  END lock_row;

  /*=====================================
   **  PROCEDURE delete_row(
  **=====================================*/
  PROCEDURE delete_row(p_my_id IN NUMBER) IS
  BEGIN
    DELETE FROM cux_test_tbl
     WHERE my_id = p_my_id;
    IF (SQL%NOTFOUND) THEN
      RAISE no_data_found;
    END IF;
  END delete_row;

END cux_test_tbl_4j_pkg;
/
