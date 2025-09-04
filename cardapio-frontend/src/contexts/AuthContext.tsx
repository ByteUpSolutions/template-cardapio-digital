import React, { createContext, useContext, useState, useEffect } from 'react';
import { UserType, LoginRequest, LoginResponse } from '../types';
import { authApi } from '../services/api';
import toast from 'react-hot-toast';

interface AuthContextType {
  isAuthenticated: boolean;
  userType: UserType | null;
  userName: string | null;
  login: (credentials: LoginRequest) => Promise<boolean>;
  logout: () => void;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userType, setUserType] = useState<UserType | null>(null);
  const [userName, setUserName] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const savedUserType = localStorage.getItem('userType') as UserType;
    const savedUserName = localStorage.getItem('userName');

    if (token && savedUserType && savedUserName) {
      setIsAuthenticated(true);
      setUserType(savedUserType);
      setUserName(savedUserName);
    }
    
    setLoading(false);
  }, []);

  const login = async (credentials: LoginRequest): Promise<boolean> => {
  try {
    setLoading(true);
    const response: LoginResponse = await authApi.login(credentials);

    // Correção: Acessar os dados dentro do objeto 'usuario'
    const userRole = response.usuario.role;
    const userName = response.usuario.nome;

    localStorage.setItem('token', response.token);
    localStorage.setItem('userType', userRole);
    localStorage.setItem('userName', userName);

    setIsAuthenticated(true);
    setUserType(userRole);
    setUserName(userName);

    toast.success('Login realizado com sucesso!');
    return true;
  } catch (error) {
    console.error('Erro no login:', error);
    // A mensagem de erro já é exibida pelo interceptor da API
    return false;
  } finally {
    setLoading(false);
  }
};

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userType');
    localStorage.removeItem('userName');
    
    setIsAuthenticated(false);
    setUserType(null);
    setUserName(null);
    
    toast.success('Logout realizado com sucesso!');
  };

  const value: AuthContextType = {
    isAuthenticated,
    userType,
    userName,
    login,
    logout,
    loading,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};