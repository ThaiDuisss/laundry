// RegisterForm.js
import React, { useState } from "react";
import RegisterStep1 from "./RegisterStep1";
import RegisterStep2 from "./RegisterStep2";
import { register } from "../../services/authenticationService";
import { Snackbar, Alert } from "@mui/material";
import { useNavigate } from "react-router-dom";

function RegisterForm() {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    fullName: "",
    phone: "",
  });

  const [snackBarOpen, setSnackBarOpen] = useState(false);
  const [snackBarMessage, setSnackBarMessage] = useState("");

  const nextStep = () => setStep(step + 1);
  const prevStep = () => setStep(step - 1);

  const handleSubmit = async () => {
    try {
      const response = await register(
        formData.email,
        formData.password,
        formData.fullName,
        formData.phone
      );
      console.log("Response body:", response);
      navigate(`/await?email=${encodeURIComponent(formData.email)}`);
    } catch (error) {
      const errorResponse = error.response?.data || { message: "Register failed" };
      setSnackBarMessage(errorResponse.message);
      setSnackBarOpen(true);
    }
  };

  const handleCloseSnackBar = (event, reason) => {
    if (reason === "clickaway") return;
    setSnackBarOpen(false);
  };

  return (
    <>
      {step === 1 && (
        <RegisterStep1
          formData={formData}
          setFormData={setFormData}
          nextStep={nextStep}
        />
      )}
      {step === 2 && (
        <RegisterStep2
          formData={formData}
          setFormData={setFormData}
          prevStep={prevStep}
          handleSubmit={handleSubmit}
        />
      )}

      <Snackbar
        open={snackBarOpen}
        autoHideDuration={4000}
        onClose={handleCloseSnackBar}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
      >
        <Alert severity="error" onClose={handleCloseSnackBar}>
          {snackBarMessage}
        </Alert>
      </Snackbar>
    </>
  );
}

export default RegisterForm;
