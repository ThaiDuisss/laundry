import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { connectSocket, getSocket } from "../configurations/socket"
import { mailAgain } from "../services/authenticationService";

function AwaitEmailConfirmation() {
  const navigate = useNavigate();
  const location = useLocation();
  const email = new URLSearchParams(location.search).get('email');

  useEffect(() => {
    connectSocket(email, 8090); // Kết nối socket với token từ localStorage
    const socket = getSocket();
    const handleValidEmail = (message) => {
      console.log('Connected to socket with ID:', socket.id);
      console.log('Received valid-email event:', message);
      navigate('/login');
    };

    socket.on('valid-email', handleValidEmail);
    return () => {
      // Gỡ listener khi component unmount
      socket.off('valid-email', handleValidEmail);

      // Nếu bạn muốn disconnect socket khi rời khỏi component
      socket.disconnect();
    };
  }, []);

  const handleResendEmail = async () => {
    try {
      const response = await mailAgain(email);
      if (response.ok) {
        alert('Confirmation email has been resent. Please check your inbox!');
      } else {
        alert('Failed to resend the confirmation email. Please try again later!');
      }
    } catch (error) {
      console.error('Lỗi gửi lại email:', error);
      alert('Đã xảy ra lỗi. Vui lòng thử lại sau!');
    }
  };

  return (
    <div className="min-h-screen flex flex-col justify-center items-center text-center px-4">
      <h2 className="text-2xl font-semibold mb-4">Please check your email to confirm your account</h2>
      <p className="mb-4">
        We have sent a confirmation email to <strong>{email}</strong>. After confirming, you will be automatically redirected.
      </p>

      <div className="spinner mb-4" />

      <p>
        If you didn't receive the email, please check your spam folder or{' '}
        <button
          onClick={handleResendEmail}
          className="text-blue-600 underline"
        >
          resend the confirmation email
        </button>.
      </p>

      <style>{`
        .spinner {
          border: 4px solid rgba(0, 0, 0, 0.1);
          border-left-color: #3498db;
          width: 40px;
          height: 40px;
          border-radius: 50%;
          animation: spin 1s linear infinite;
        }
        @keyframes spin {
          100% {
            transform: rotate(360deg);
          }
        }
      `}</style>
    </div>
  );
}

export default AwaitEmailConfirmation;
