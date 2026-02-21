<?php

require '../../config/db.php';
require '../guard.php';


// ambil semua order terbaru dulu
$stmt = $conn->prepare("
    SELECT
        id,
        email,
        total,
        status,
        created_at
    FROM orders
    ORDER BY created_at DESC
");

$stmt->execute();

$result = $stmt->get_result();

?>

<!DOCTYPE html>
<html>
<head>
    <title>Admin - Orders</title>
</head>
<body>

<h2>Orders</h2>

<table border="1" cellpadding="8">

<tr>
    <th>ID</th>
    <th>Email</th>
    <th>Total</th>
    <th>Status</th>
    <th>Date</th>
    <th>Action</th>
</tr>

<?php while ($row = $result->fetch_assoc()): ?>

<tr>

    <td>
        <?= $row['id'] ?>
    </td>

    <td>
        <?= htmlspecialchars($row['email']) ?>
    </td>

    <td>
        Rp <?= number_format($row['total']) ?>
    </td>

    <td>
        <?= $row['status'] ?>
    </td>

    <td>
        <?= $row['created_at'] ?>
    </td>

    <td>
        <a href="detail.php?id=<?= $row['id'] ?>">
            Detail
        </a>
    </td>

</tr>

<?php endwhile; ?>

</table>

</body>
</html>