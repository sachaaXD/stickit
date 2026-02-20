<?php

header("Content-Type: application/json");
require '../../config/db.php';
require '../guard.php';

$user = require_admin($conn);

$target_dir = "../../uploads/stickers/";

if (!is_dir($target_dir)) {
    mkdir($target_dir, 0777, true);
}


if (!isset($_FILES["image"])) {

    echo json_encode([
        "status" => "error",
        "message" => "File required"
    ]);

    exit;
}


$name = trim($_POST["name"] ?? '');
$category = trim($_POST["category"] ?? 'general');
$uploaded_by = trim($_POST["uploaded_by"] ?? 'admin');

if ($name === '') {

    echo json_encode([
        "status" => "error",
        "message" => "Name required"
    ]);

    exit;
}


$file = $_FILES["image"];

$max_size = 2 * 1024 * 1024;

if ($file["size"] > $max_size) {

    echo json_encode([
        "status" => "error",
        "message" => "Max size 2MB"
    ]);

    exit;
}


$allowed = ["image/png", "image/jpeg", "image/jpg"];

if (!in_array($file["type"], $allowed)) {

    echo json_encode([
        "status" => "error",
        "message" => "Only PNG/JPG allowed"
    ]);

    exit;
}


$filename = time() . "_" . basename($file["name"]);

$target_file = $target_dir . $filename;


if (!move_uploaded_file($file["tmp_name"], $target_file)) {

    echo json_encode([
        "status" => "error",
        "message" => "Upload failed"
    ]);

    exit;
}


$image_url = "uploads/stickers/" . $filename;


$stmt = $conn->prepare("
    INSERT INTO upload
    (name, image_url, category, uploaded_by)
    VALUES (?, ?, ?, ?)
");

$stmt->bind_param("ssss", $name, $image_url, $category, $uploaded_by);


if ($stmt->execute()) {

    echo json_encode([
        "status" => "success",
        "message" => "Upload success",
        "image_url" => $image_url
    ]);

} else {

    echo json_encode([
        "status" => "error",
        "message" => "DB insert failed"
    ]);
}


$stmt->close();
$conn->close();