<?php
header("Content-Type: application/json");

require_once "../../config/db.php";

// folder tujuan upload
$target_dir = "../../uploads/stickers/";

// cek apakah folder ada
if (!is_dir($target_dir)) {
    mkdir($target_dir, 0777, true);
}

// cek file dikirim atau tidak
if (!isset($_FILES["image"])) {
    echo json_encode([
        "success" => false,
        "message" => "Tidak ada file dikirim"
    ]);
    exit;
}

$name = $_POST["name"] ?? "";
$category = $_POST["category"] ?? "general";
$uploaded_by = $_POST["uploaded_by"] ?? "admin";

$file = $_FILES["image"];

$max_size = 2 * 1024 * 1024; // 2MB

// validasi size
if ($file["size"] > $max_size) {
    echo json_encode([
        "success" => false,
        "message" => "Ukuran file terlalu besar (max 2MB)"
    ]);
    exit;
}

// validasi tipe
$allowed_types = ["image/png", "image/jpeg", "image/jpg"];

if (!in_array($file["type"], $allowed_types)) {
    echo json_encode([
        "success" => false,
        "message" => "Format harus PNG atau JPG"
    ]);
    exit;
}

// buat nama file unik
$filename = time() . "_" . basename($file["name"]);

$target_file = $target_dir . $filename;

// upload file
if (move_uploaded_file($file["tmp_name"], $target_file)) {

    // path untuk database
    $image_url = "uploads/stickers/" . $filename;

    // simpan ke database
    $stmt = $conn->prepare("
    INSERT INTO upload
    (name, image_url, category, uploaded_by)
    VALUES (?, ?, ?, ?)
");


    $stmt->bind_param("ssss", $name, $image_url, $category, $uploaded_by);

    if ($stmt->execute()) {

        echo json_encode([
            "success" => true,
            "message" => "Upload berhasil",
            "image_url" => $image_url
        ]);

    } else {

        echo json_encode([
            "success" => false,
            "message" => "Gagal simpan ke database"
        ]);
    }

} else {

    echo json_encode([
        "success" => false,
        "message" => "Upload file gagal"
    ]);
}
