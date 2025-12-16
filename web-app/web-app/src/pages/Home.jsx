import { useEffect, useState, useCallback } from "react";
import { useDebounce } from "use-debounce";
import {
  Box,
  Card,
  Typography,
  CircularProgress,
  TextField,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
  Button,
} from "@mui/material";
import Scene from "./Scene";
import { getProducts } from "../services/productService";
import { categories } from "../data/category";
import { services } from "../data/service";
import { useNavigate } from "react-router-dom";
import { isAuthenticated } from "../services/authenticationService";

export default function Home() {
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState(0);
  const [keywordFilter, setKeywordFilter] = useState("");
  const navigate = useNavigate();

  // ✅ debounce keyword
  const [debouncedKeyword] = useDebounce(keywordFilter, 500);
  const PAGE_SIZE = 5;

  const loadProducts = useCallback(
    (page, category = categoryFilter, keyword = debouncedKeyword) => {
      setLoading(true);
      getProducts({ page, size: PAGE_SIZE, category, keyword })
        .then((res) => {
          const result = res.data;
          setTotalPages(result.totalPages);
          setProducts(result.content);
        })
        .finally(() => setLoading(false));
    },
    [categoryFilter, debouncedKeyword]
  );



  // ✅ reset khi filter/search đổi
  useEffect(() => {
    setPage(0);
    loadProducts(0, categoryFilter, debouncedKeyword);

  }, [categoryFilter, debouncedKeyword, loadProducts]);

  // ✅ gọi API khi page thay đổi
  useEffect(() => {
    loadProducts(page, categoryFilter, debouncedKeyword);
  }, [page, categoryFilter, debouncedKeyword, loadProducts]);

  return (
    <Scene>
      <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}>
        {/* Filters */}
        <Box sx={{ display: "flex", gap: 2, mb: 2 }}>
          <FormControl sx={{ minWidth: 180 }}>
            <InputLabel>Category</InputLabel>
            <Select
              value={categoryFilter}
              label="Category"
              onChange={(e) => setCategoryFilter(Number(e.target.value))}
            >
              <MenuItem value={0}>All</MenuItem>
              {categories.map((cat) => (
                <MenuItem key={cat.id} value={cat.id}>
                  {cat.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <TextField
            label="Search"
            value={keywordFilter}
            onChange={(e) => setKeywordFilter(e.target.value)}
          />
        </Box>

        {/* Product list */}
        {products.map((product) => {
          const categoryName =
            categories.find((cat) => cat.id === product.categoryId)?.name ||
            "Unknown";

          return (
            <Card
              key={product.id}
              sx={{
                p: 2,
                display: "flex",
                alignItems: "center",
                gap: 2,
                cursor: "pointer",
              }}
              onClick={() => navigate(`/product/${product.id}`)} // ✅ chuyển sang trang chi tiết
            >
              <img
                src={
                  product.avatar.startsWith("http")
                    ? product.avatar
                    : `http://localhost:8080/files/media/download/${product.avatar}`
                }
                alt={product.name}
                style={{
                  width: 80,
                  height: 80,
                  objectFit: "cover",
                  borderRadius: 5,
                }}
              />
              <Box sx={{ display: "flex", flexDirection: "column" }}>
                <Typography variant="subtitle1">{product.name}</Typography>
                <Typography variant="body2">Category: {categoryName}</Typography>
                <Typography variant="body2">
                  Prices:{" "}
                  {Object.entries(product.prices)
                    .map(([k, v]) => {
                      const service = services[k];
                      return service ? `(${service.code}): ${v}` : ` ${v}`;
                    })
                    .join(", ")}
                </Typography>
              </Box>
            </Card>
          );
        })}

        {loading && (
          <Box sx={{ display: "flex", justifyContent: "center", mt: 2 }}>
            <CircularProgress size={24} />
          </Box>
        )}

        {/* Pagination */}
        <Box sx={{ display: "flex", justifyContent: "center", gap: 2, mt: 3 }}>
          <Button
            variant="outlined"
            disabled={page <= 0}
            onClick={() => setPage((p) => p - 1)}
          >
            Prev
          </Button>
          <Typography sx={{ alignSelf: "center" }}>
            Page {page + 1} / {totalPages}
          </Typography>
          <Button
            variant="outlined"
            disabled={page >= totalPages - 1}
            onClick={() => setPage((p) => p + 1)}
          >
            Next
          </Button>
        </Box>
      </Box>
    </Scene>
  );
}
