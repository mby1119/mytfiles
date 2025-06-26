/*
 Navicat Premium Dump SQL

 Source Server         : we
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : mymusic

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 26/06/2025 15:40:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ddorder
-- ----------------------------
DROP TABLE IF EXISTS `ddorder`;
CREATE TABLE `ddorder`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `total` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `orderno` int NULL DEFAULT NULL,
  `paytime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ddorder
-- ----------------------------
INSERT INTO `ddorder` VALUES (1, '1', '1', 1, NULL);
INSERT INTO `ddorder` VALUES (2, '2', '1', 1, NULL);
INSERT INTO `ddorder` VALUES (3, '12', '12', NULL, NULL);
INSERT INTO `ddorder` VALUES (4, '13', '13', NULL, NULL);
INSERT INTO `ddorder` VALUES (5, '14', '14', NULL, NULL);
INSERT INTO `ddorder` VALUES (6, '15', '15', NULL, NULL);
INSERT INTO `ddorder` VALUES (7, '161', '16', NULL, NULL);
INSERT INTO `ddorder` VALUES (8, '12', '12', NULL, NULL);
INSERT INTO `ddorder` VALUES (9, '11', '11', NULL, NULL);
INSERT INTO `ddorder` VALUES (10, '12', '12', NULL, NULL);
INSERT INTO `ddorder` VALUES (11, '11', '111', NULL, NULL);

-- ----------------------------
-- Table structure for gedan
-- ----------------------------
DROP TABLE IF EXISTS `gedan`;
CREATE TABLE `gedan`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `musicid` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gedan
-- ----------------------------

-- ----------------------------
-- Table structure for music
-- ----------------------------
DROP TABLE IF EXISTS `music`;
CREATE TABLE `music`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `audiourl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `singer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `album` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of music
-- ----------------------------
INSERT INTO `music` VALUES (22, '不眠之夜', 'http://localhost:8081/files/download/1749626969194_张杰 - 不眠之夜.mp3', '张杰 ', NULL, 'http://localhost:8081/files/download/1749626995609_12.png', NULL);
INSERT INTO `music` VALUES (23, '使一颗心免于哀伤', 'http://localhost:8081/files/download/1749627019361_知更鸟、HOYO-MiX、Chevy - 使一颗心免于哀伤.mp3', 'Chevy', NULL, 'http://localhost:8081/files/download/1749627002555_1744029372633.png', NULL);

-- ----------------------------
-- Table structure for playlist
-- ----------------------------
DROP TABLE IF EXISTS `playlist`;
CREATE TABLE `playlist`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `musicid` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of playlist
-- ----------------------------
INSERT INTO `playlist` VALUES (62, 22);
INSERT INTO `playlist` VALUES (63, 23);

SET FOREIGN_KEY_CHECKS = 1;
