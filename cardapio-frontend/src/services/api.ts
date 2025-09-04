import axios, { AxiosResponse } from 'axios';
import toast from 'react-hot-toast';
import { 
  LoginRequest, 
  LoginResponse, 
  User, 
  ItemCardapio, 
  Pedido, 
  PedidoStatus,
  ApiResponse 
} from '../types';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token de autenticação
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor para tratar erros
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('userType');
      localStorage.removeItem('userName');
      window.location.href = '/login';
    }
    
    const message = error.response?.data?.message || 'Erro na requisição';
    toast.error(message);
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    const response: AxiosResponse<LoginResponse> = await api.post('/api/auth/login', credentials);
    return response.data;
  },
  
  register: async (userData: Partial<User>): Promise<User> => {
    const response: AxiosResponse<User> = await api.post('/api/auth/register', userData);
    return response.data;
  }
};

// Menu API  
export const menuApi = {
  getItens: async (): Promise<ItemCardapio[]> => {
    const response: AxiosResponse<ItemCardapio[]> = await api.get('/api/admin/cardapio');
    return response.data;
  },
  
  createItem: async (item: Omit<ItemCardapio, 'id'>): Promise<ItemCardapio> => {
    const response: AxiosResponse<ItemCardapio> = await api.post('/api/admin/cardapio', item);
    return response.data;
  },
  
  updateItem: async (id: string, item: Partial<ItemCardapio>): Promise<ItemCardapio> => {
    const response: AxiosResponse<ItemCardapio> = await api.put(`/api/admin/cardapio/${id}`, item);
    return response.data;
  },
  
  deleteItem: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/cardapio/${id}`);
  },

  getByCategoria: async (categoria: string): Promise<ItemCardapio[]> => {
    const response: AxiosResponse<ItemCardapio[]> = await api.get(`/api/menu/categoria/${categoria}`);
    return response.data;
  }
};

// Pedidos API
export const pedidosApi = {
  create: async (pedido: Partial<Pedido>): Promise<Pedido> => {
    const response: AxiosResponse<Pedido> = await api.post('/api/cliente/pedidos', pedido);
    return response.data;
  },
  
  getAll: async (): Promise<Pedido[]> => {
    const response: AxiosResponse<Pedido[]> = await api.get('/api/admin/pedidos');
    return response.data;
  },
  
  getByCliente: async (clienteId: string): Promise<Pedido[]> => {
    const response: AxiosResponse<Pedido[]> = await api.get(`/api/cliente/pedidos/${clienteId}`);
    return response.data;
  },
  
  getByCozinha: async (): Promise<Pedido[]> => {
    const response: AxiosResponse<Pedido[]> = await api.get('/api/cozinha/pedidos');
    return response.data;
  },
  
  getByGarcom: async (): Promise<Pedido[]> => {
    const response: AxiosResponse<Pedido[]> = await api.get('/api/garcom/pedidos');
    return response.data;
  },
  
  updateStatus: async (id: string, status: PedidoStatus): Promise<void> => {
    await api.patch(`/api/cozinha/pedidos/${id}/status`, { status });
  },
  
  atribuirGarcom: async (pedidoId: string, garcomId: string): Promise<void> => {
    await api.patch(`/api/garcom/pedidos/${pedidoId}/atribuir`, { garcomId });
  },
  
  finalizarEntrega: async (pedidoId: string): Promise<void> => {
    await api.patch(`/api/garcom/pedidos/${pedidoId}/finalizar`);
  }
};

// Usuários API
export const usuariosApi = {
  getAll: async (): Promise<User[]> => {
    const response: AxiosResponse<User[]> = await api.get('/api/admin/usuarios');
    return response.data;
  },
  
  create: async (user: Omit<User, 'id'>): Promise<User> => {
    const response: AxiosResponse<User> = await api.post('/api/admin/usuarios', user);
    return response.data;
  },
  
  update: async (id: string, user: Partial<User>): Promise<User> => {
    const response: AxiosResponse<User> = await api.put(`/api/admin/usuarios/${id}`, user);
    return response.data;
  },
  
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/admin/usuarios/${id}`);
  },
  
  toggleStatus: async (id: string): Promise<void> => {
    await api.patch(`/api/admin/usuarios/${id}/toggle-status`);
  }
};

export { api };