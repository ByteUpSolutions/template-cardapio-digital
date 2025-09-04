import React, { createContext, useContext, useState, useEffect } from 'react';
import { CartItem, ItemCardapio } from '../types';

interface CartContextType {
  items: CartItem[];
  addItem: (item: ItemCardapio, quantidade?: number, observacoes?: string) => void;
  removeItem: (itemId: string) => void;
  updateQuantity: (itemId: string, quantidade: number) => void;
  clearCart: () => void;
  getTotalItems: () => number;
  getTotalPrice: () => number;
}

const CartContext = createContext<CartContextType | null>(null);

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

interface CartProviderProps {
  children: React.ReactNode;
}

export const CartProvider: React.FC<CartProviderProps> = ({ children }) => {
  const [items, setItems] = useState<CartItem[]>([]);

  // Carregar carrinho do localStorage
  useEffect(() => {
    const savedCart = localStorage.getItem('cart');
    if (savedCart) {
      setItems(JSON.parse(savedCart));
    }
  }, []);

  // Salvar carrinho no localStorage
  useEffect(() => {
    localStorage.setItem('cart', JSON.stringify(items));
  }, [items]);

  const addItem = (item: ItemCardapio, quantidade = 1, observacoes = '') => {
    setItems(prev => {
      const existingIndex = prev.findIndex(cartItem => 
        cartItem.id === item.id && cartItem.observacoes === observacoes
      );

      if (existingIndex >= 0) {
        const updated = [...prev];
        updated[existingIndex].quantidade += quantidade;
        return updated;
      }

      return [...prev, { ...item, quantidade, observacoes }];
    });
  };

  const removeItem = (itemId: string) => {
    setItems(prev => prev.filter(item => item.id !== itemId));
  };

  const updateQuantity = (itemId: string, quantidade: number) => {
    if (quantidade <= 0) {
      removeItem(itemId);
      return;
    }

    setItems(prev => prev.map(item => 
      item.id === itemId ? { ...item, quantidade } : item
    ));
  };

  const clearCart = () => {
    setItems([]);
  };

  const getTotalItems = () => {
    return items.reduce((total, item) => total + item.quantidade, 0);
  };

  const getTotalPrice = () => {
    return items.reduce((total, item) => total + (item.preco * item.quantidade), 0);
  };

  const value: CartContextType = {
    items,
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    getTotalItems,
    getTotalPrice,
  };

  return (
    <CartContext.Provider value={value}>
      {children}
    </CartContext.Provider>
  );
};