import {
  Box,
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
  Divider,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

function RegisterStep1({ formData, setFormData, nextStep }) {
  const navigate = useNavigate();

  const [confirmError, setConfirmError] = useState("");

  const handleContinue = () => {
    if (
      formData.email &&
      /\S+@\S+\.\S+/.test(formData.email) &&
      formData.password &&
      formData.password.length >= 6 &&
      formData.confirmPassword === formData.password
    ) {
      nextStep();
    } else {
      if (formData.confirmPassword !== formData.password) {
        setConfirmError("Passwords do not match");
      }
    }
  };

  return (
    <Box
      display="flex"
      alignItems="center"
      justifyContent="center"
      height="100vh"
      bgcolor={"#f0f2f5"}
    >
      <Card
        sx={{
          minWidth: 300,
          maxWidth: 400,
          boxShadow: 3,
          borderRadius: 3,
          padding: 4,
        }}
      >
        <CardContent>
          <Typography variant="h5" gutterBottom>
            Step 1: Account Information
          </Typography>

          <TextField
            label="Email"
            type="email"
            variant="outlined"
            fullWidth
            margin="normal"
            required
            value={formData.email}
            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          />

          <TextField
            label="Password"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            required
            inputProps={{ minLength: 6 }}
            value={formData.password}
            onChange={(e) =>
              setFormData({ ...formData, password: e.target.value })
            }
          />

          <TextField
            label="Confirm Password"
            type="password"
            variant="outlined"
            fullWidth
            margin="normal"
            required
            value={formData.confirmPassword}
            onChange={(e) => {
              setFormData({ ...formData, confirmPassword: e.target.value });
              if (e.target.value !== formData.password) {
                setConfirmError("Passwords do not match");
              } else {
                setConfirmError("");
              }
            }}
            error={!!confirmError}
            helperText={confirmError}
          />

          <Button
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 3, mb: 2 }}
            onClick={handleContinue}
          >
            Continue
          </Button>

          <Divider sx={{ my: 2 }} />

          <Typography variant="body2" align="center">
            Already have an account?{" "}
            <Button
              color="secondary"
              onClick={() => navigate("/login")}
              sx={{ textTransform: "none", p: 0 }}
            >
              Login
            </Button>
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
}

export default RegisterStep1;
