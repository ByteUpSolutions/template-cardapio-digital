export interface User {
  id: string;
  email: string;
  nome: string;
  telefone?: string;
  tipo: UserType;
  ativo: boolean;
}

export enum UserType {
  ADMIN = 'ADMIN',
  CLIENTE = 'CLIENTE',
  COZINHA = 'COZINHA',
  GARCOM = 'GARCOM'
}

export interface ItemCardapio {
  id: string;
  nome: string;
  descricao: string;
  preco: number;
  categoria: string;
  disponivel: boolean;
  imagemUrl?: string;
  tempoPreparoMinutos?: number;
}

export interface Pedido {
  id: string;
  clienteId: string;
  clienteNome?: string;
  status: PedidoStatus;
  dataHora: string;
  valorTotal: number;
  observacoes?: string;
  itens: PedidoItem[];
  mesa?: number;
  garcomId?: string;
  garcomNome?: string;
}

export interface PedidoItem {
  id: string;
  itemCardapioId: string;
  itemNome: string;
  quantidade: number;
  precoUnitario: number;
  observacoes?: string;
}

export enum PedidoStatus {
  PENDENTE = 'PENDENTE',
  CONFIRMADO = 'CONFIRMADO',
  EM_PREPARO = 'EM_PREPARO',
  PRONTO = 'PRONTO',
  ENTREGUE = 'ENTREGUE',
  CANCELADO = 'CANCELADO'
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  tipo: string; // O tipo do token, ex: "Bearer"
  usuario: {
    id: number;
    nome: string;
    email: string;
    role: UserType;
  };
}

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  message?: string;
}

export interface CartItem extends ItemCardapio {
  quantidade: number;
  observacoes?: string;
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}