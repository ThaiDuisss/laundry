import {
  Box,
  Button,
  Card,
  CardContent,
  TextField,
  Typography,
} from "@mui/material";
import { useState } from "react";

function RegisterStep2({ formData, setFormData, prevStep, handleSubmit }) {
  const [errors, setErrors] = useState({});

  const validate = () => {
    let newErrors = {};

    if (!formData.fullName) {
      newErrors.fullName = "Full Name is required";
    }

    if (!formData.phone) {
      newErrors.phone = "Phone Number is required";
    } else if (!/^[0-9]{8,15}$/.test(formData.phone)) {
      newErrors.phone = "Invalid phone number";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleRegister = () => {
    if (validate()) {
      handleSubmit();
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
            Step 2: Personal Information
          </Typography>

          <Box
            component="form"
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            width="100%"
          >
            <TextField
              label="Full Name"
              variant="outlined"
              fullWidth
              margin="normal"
              required
              value={formData.fullName}
              onChange={(e) =>
                setFormData({ ...formData, fullName: e.target.value })
              }
              error={!!errors.fullName}
              helperText={errors.fullName}
            />

            <TextField
              label="Phone Number"
              variant="outlined"
              fullWidth
              margin="normal"
              required
              value={formData.phone}
              onChange={(e) =>
                setFormData({ ...formData, phone: e.target.value })
              }
              error={!!errors.phone}
              helperText={errors.phone}
            />

            <Button
              variant="outlined"
              color="secondary"
              fullWidth
              sx={{ mt: 3, mb: 1 }}
              onClick={prevStep}
            >
              Back
            </Button>

            <Button
              variant="contained"
              color="primary"
              fullWidth
              sx={{ mt: 1 }}
              onClick={handleRegister}
            >
              Register
            </Button>
          </Box>
        </CardContent>
      </Card>
    </Box>
  );
}

export default RegisterStep2;
