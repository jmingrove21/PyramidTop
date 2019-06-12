
<script language="javascript">
data='<?php echo $userName; ?>';
alert(data);
</script>

<?php
$data_back = json_decode(file_get_contents('php://input'));

$userName = $data_back->{"reqCoordType"};
?>

