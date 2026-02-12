<?php
// Rapihin output
ini_set('display_errors', 0);
ini_set('display_startup_errors', 0);
error_reporting(E_ALL);

$host = "localhost";
$user = "root";
$pass = "root";
$db   = "sticker_db";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
 
    header('Content-Type: application/json');
    echo json_encode([
        'status' => 'error',
        'message' => 'Database connection failed'
    ]);
    exit;
}

$conn->set_charset("utf8mb4");
