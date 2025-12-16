import { getToken, removeToken, setToken } from "./localStorageService";
import httpClient from "../configurations/httpClient";
import { API } from "../configurations/configuration";

export const logIn = async (username, password) => {
  const response = await httpClient.post(API.LOGIN, {
    username: username,
    password: password,
  });
console.log("Login response:", response.data);
  setToken(response.data);
  
  return response;
};
export const mailAgain = async (email) => {
  
  return await httpClient.get(`${API.MAIL_AGAIN}/${email}`);
};
export const register = async (username, password, fullName, phoneNumber) => {
  const response = await httpClient.post(API.REGISTER, {
    username: username,
    password: password,
    fullName:fullName,
    phoneNumber: phoneNumber
  });
  return response
}

export const logOut = () => {
  removeToken();
};

export const isAuthenticated = () => {
  return getToken();
};
