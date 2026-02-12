<?php
// Pastikan file endpoint admin sudah require db.php sebelum memanggil fungsi inii

function require_admin($conn) {
    $user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : 0;

    if ($user_id <= 0) {
        echo json_encode(['status' => 'error', 'message' => 'user_id required (admin)']);
        exit;
    }

    $stmt = $conn->prepare("SELECT role FROM users WHERE id = ?");
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $res = $stmt->get_result();
    $user = $res->fetch_assoc();
    $stmt->close();

    if (!$user || $user['role'] !== 'admin') {
        echo json_encode(['status' => 'error', 'message' => 'Unauthorized (admin only)']);
        exit;
    }

    return $user_id;
}
