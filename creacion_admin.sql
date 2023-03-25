-- INSERTAR ADMIN
LOCK TABLES `guarderiacentralfinal`.`usuario` WRITE;
INSERT INTO `guarderiacentralfinal`.`usuario` VALUES (1,'Prueba 123',12123123,'Administrador','admin','1234-1234','admin');
UNLOCK TABLES;


-- INSERTO ID ADMIN
LOCK TABLES `guarderiacentralfinal`.`administrador` WRITE;
INSERT INTO `guarderiacentralfinal`.`administrador` VALUES (1);
UNLOCK TABLES;
