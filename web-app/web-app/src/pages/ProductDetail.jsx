import { useParams, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Dialog, DialogTitle, DialogContent, DialogActions } from "@mui/material";
import {
  Box,
  Card,
  Typography,
  Button,
  CircularProgress,
  Divider,
  IconButton,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import { getProductById } from "../services/productService";
import { categories } from "../data/category";
import { services } from "../data/service";
import { addCart } from "../services/orderSevice"

export default function ProductDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
 const [openDialog, setOpenDialog] = useState(false);
  const [selectedService, setSelectedService] = useState(null);

  const handleOpenDialog = () => setOpenDialog(true);
  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedService(null);
  };
  useEffect(() => {
    getProductById(id).then((res) => {
      setProduct(res.data);
      setLoading(false);
    });
  }, [id]);

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (!product) {
    return (
      <Typography sx={{ mt: 4, textAlign: "center" }}>
        Product not found
      </Typography>
    );
  }
  const handleConfirmAddToCart = async () => {
    if (!selectedService) {
      alert("Vui lòng chọn dịch vụ trước khi thêm vào giỏ!");
      return;
    }
    try {
      const response = await addCart({
        productId: id,
        name: `${product.name} - ${services[selectedService.key]?.name}`,
        prices: selectedService.price,
        avatar: product.avatar,
      });
      console.log("OKEBB", response)
      alert(response.status);
      handleCloseDialog();
    } catch (err) {
      console.error("❌ Lỗi thêm vào giỏ:", err);
      alert("Có lỗi xảy ra khi thêm vào giỏ hàng");
    }
  };
  return (
    <Box sx={{ position: "relative" }}>
      {/* Nút quay lại góc trái */}
      <IconButton
        onClick={() => navigate(-1)}
        sx={{
          position: "absolute",
          top: 16,
          left: 16,
          backgroundColor: "white",
          border: "1px solid #f0f0f0",
          "&:hover": {
            backgroundColor: "rgba(245,61,45,0.05)",
          },
        }}
      >
        <ArrowBackIcon sx={{ color: "#f53d2d" }} />
      </IconButton>

      <Card
        sx={{
          p: 4,
          mt: 6,
          maxWidth: 1100,
          mx: "auto",
          borderRadius: 2,
          boxShadow: "0 2px 12px rgba(0,0,0,0.1)",
        }}
      >
        <Box sx={{ display: "flex", gap: 5 }}>
          {/* Ảnh sản phẩm */}
          <Box
            sx={{
              flex: "0 0 380px",
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              border: "1px solid black", // chỉ 1 viền đen
              borderRadius: 2,
              p: 2,
              height: 380, // cố định khung vuông
              overflow: "hidden",
              backgroundColor: "#fff",
            }}
          >
            <img
              src={
                product.avatar?.startsWith("http")
                  ? product.avatar
                  : `http://localhost:8080/files/media/download/${product.avatar}`
              }
              alt={product.name}
              style={{
                width: "100%",
                height: "100%",
                objectFit: "contain",
              }}
            />
          </Box>
          {/* Thông tin sản phẩm */}
          <Box sx={{ flex: 1, display: "flex", flexDirection: "column", gap: 2 }}>
            {/* Tên */}
            <Typography variant="h5" fontWeight="bold">
              {product.name}
            </Typography>

            {/* Danh mục */}
            <Typography variant="body2" sx={{ color: "gray" }}>
              Category:{" "}
              {(() => {
                const cat =
                  categories.find(
                    (c) =>
                      c.id === Number(id)
                  ) || null;
                return cat ? cat.name : "Unknown";
              })()}
            </Typography>

            <Divider sx={{ my: 2 }} />

            {/* Giá theo từng dịch vụ */}
            <Typography variant="subtitle1" fontWeight="bold" sx={{ mb: 1 }}>
              Bảng giá:
            </Typography>
            <List dense>
              {Object.entries(product.prices).map(([k, v]) => {
                const service = services[k];
                return (
                  <ListItem
                    key={k}
                    sx={{
                      px: 0,
                      display: "flex",
                      justifyContent: "space-between",
                      borderBottom: "1px solid #f5f5f5",
                    }}
                  >
                    <ListItemText
                      primary={service ? service.name : k}
                      sx={{ color: "gray" }}
                    />
                    <Typography
                      variant="body1"
                      sx={{ color: "#f53d2d", fontWeight: "bold" }}
                    >
                      {v}₫
                    </Typography>
                  </ListItem>
                );
              })}
            </List>

            {/* Mô tả */}
            <Typography variant="body1" sx={{ mt: 2, lineHeight: 1.6 }}>
              {product.description || "No description available"}
            </Typography>

            {/* Nút hành động */}
            {/* Nút hành động */}
            <Box sx={{ display: "flex", gap: 3, mt: 4 }}>
              {/* Nút thuê dịch vụ */}
              <Button
                variant="contained"
                startIcon={<ShoppingCartIcon />}
                sx={{
                  backgroundColor: "#f53d2d",
                  "&:hover": { backgroundColor: "#d73227" },
                  px: 5,
                  py: 1.8,
                  fontSize: "1.1rem",
                  fontWeight: "bold",
                  borderRadius: 1,
                }}
                onClick={() => alert("🛒 Thuê dịch vụ thành công!")}
              >
                Thuê dịch vụ
              </Button>

              {/* Nút thêm vào giỏ */}
              <Button
                variant="outlined"
                startIcon={<ShoppingCartIcon />}
                sx={{
                  borderColor: "#f53d2d",
                  color: "#f53d2d",
                  "&:hover": {
                    borderColor: "#d73227",
                    backgroundColor: "rgba(245,61,45,0.05)",
                  },
                  px: 4,
                  py: 1.8,
                  fontSize: "1.1rem",
                  fontWeight: "bold",
                  borderRadius: 1,
                }}
                onClick={handleOpenDialog}
              >
                Thêm vào giỏ
              </Button>
              <Dialog open={openDialog} onClose={handleCloseDialog} fullWidth>
        <DialogTitle>Chọn dịch vụ cho {product.name}</DialogTitle>
        <DialogContent>
          <List>
            {Object.entries(product.prices).map(([key, price]) => {
              const service = services[key];
              return (
                <ListItem
                  button
                  key={key}
                  selected={selectedService?.key === key}
                  onClick={() => setSelectedService({ key, price })}
                >
                  <ListItemText
                    primary={service ? service.name : key}
                    secondary={`${price}₫`}
                  />
                </ListItem>
              );
            })}
          </List>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Hủy</Button>
          <Button
            onClick={handleConfirmAddToCart}
            variant="contained"
            sx={{ backgroundColor: "#f53d2d", "&:hover": { backgroundColor: "#d73227" } }}
          >
            Xác nhận
          </Button>
        </DialogActions>
      </Dialog>
            </Box>

          </Box>
        </Box>
      </Card>
    </Box>
  );
}
