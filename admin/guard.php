<?php

require_once __DIR__ . '/../config/auth.php';

function require_admin($conn)
{
    $user = get_user_from_token($conn);

    if ($user['role'] !== 'admin') {

        echo json_encode([
            'status' => 'error',
            'message' => 'Unauthorized (admin only)'
        ]);

        exit;
    }

    return $user;
}