<?php
header('Content-Type: application/json');
require 'config/db.php';

$data = json_decode(file_get_contents('php://input'), true);

if (!is_array($data)) {
    echo json_encode(['status' => 'error', 'message' => 'Invalid JSON']);
    exit;
}

$name = isset($data['name']) ? trim($data['name']) : '';
$raw_password = isset($data['password']) ? $data['password'] : '';

if ($name === '' || $raw_password === '') {
    echo json_encode(['status' => 'error', 'message' => 'Name and password required']);
    exit;
}

// role jangan ditentukan dari input (biar user tidak bisa jadi admin sendiri)
$role = 'user';

// cek username duplikat
$check = $conn->prepare("SELECT id FROM users WHERE name = ?");
$check->bind_param("s", $name);
$check->execute();
$exists = $check->get_result()->num_rows > 0;
$check->close();

if ($exists) {
    echo json_encode(['status' => 'error', 'message' => 'Username already exists']);
    exit;
}

// hash password
$password = password_hash($raw_password, PASSWORD_DEFAULT);

// insert user
$stmt = $conn->prepare("INSERT INTO users (name, password, role) VALUES (?, ?, ?)");
$stmt->bind_param("sss", $name, $password, $role);

if ($stmt->execute()) {
    echo json_encode([
        'status' => 'success',
        'message' => 'User registered'
    ]);
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Failed to register user'
    ]);
}

$stmt->close();
$conn->close();
