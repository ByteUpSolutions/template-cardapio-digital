import React from 'react';
import { useCart } from '../../contexts/CartContext';
import { Minus, Plus, Trash2, ShoppingBag } from 'lucide-react';
import { formatCurrency } from '../../utils/formatters';
import { pedidosApi } from '../../services/api';
import toast from 'react-hot-toast';

const CartView: React.FC = () => {
  const { 
    items, 
    updateQuantity, 
    removeItem, 
    clearCart, 
    getTotalItems, 
    getTotalPrice 
  } = useCart();

  const handleCheckout = async () => {
    if (items.length === 0) {
      toast.error('Carrinho vazio!');
      return;
    }

    try {
      const pedidoData = {
        itens: items.map(item => ({
          itemCardapioId: item.id,
          quantidade: item.quantidade,
          precoUnitario: item.preco,
          observacoes: item.observacoes
        })),
        valorTotal: getTotalPrice(),
        observacoes: 'Pedido feito pelo sistema web'
      };

      await pedidosApi.create(pedidoData);
      toast.success('Pedido realizado com sucesso!');
      clearCart();
    } catch (error) {
      console.error('Erro ao realizar pedido:', error);
    }
  };

  if (items.length === 0) {
    return (
      <div className="text-center py-12">
        <ShoppingBag className="w-16 h-16 text-gray-400 mx-auto mb-4" />
        <h3 className="text-lg font-medium text-gray-900 mb-2">Carrinho vazio</h3>
        <p className="text-gray-500">Adicione itens do cardápio ao seu carrinho.</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Carrinho de Compras</h2>
        <span className="text-lg text-gray-600">{getTotalItems()} itens</span>
      </div>

      <div className="bg-white rounded-lg shadow-md">
        {items.map((item) => (
          <div key={`${item.id}-${item.observacoes}`} className="p-6 border-b last:border-b-0">
            <div className="flex items-center space-x-4">
              {item.imagemUrl && (
                <img
                  src={item.imagemUrl}
                  alt={item.nome}
                  className="w-16 h-16 object-cover rounded-md"
                />
              )}
              
              <div className="flex-1">
                <h4 className="font-semibold text-gray-900">{item.nome}</h4>
                <p className="text-sm text-gray-600">{item.categoria}</p>
                {item.observacoes && (
                  <p className="text-xs text-gray-500 mt-1">
                    Obs: {item.observacoes}
                  </p>
                )}
                <p className="text-lg font-bold text-green-600 mt-1">
                  {formatCurrency(item.preco)}
                </p>
              </div>

              <div className="flex items-center space-x-3">
                <button
                  onClick={() => updateQuantity(item.id, item.quantidade - 1)}
                  className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors duration-200"
                >
                  <Minus className="w-4 h-4" />
                </button>
                
                <span className="font-medium w-8 text-center">{item.quantidade}</span>
                
                <button
                  onClick={() => updateQuantity(item.id, item.quantidade + 1)}
                  className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors duration-200"
                >
                  <Plus className="w-4 h-4" />
                </button>

                <button
                  onClick={() => removeItem(item.id)}
                  className="w-8 h-8 rounded-full bg-red-100 hover:bg-red-200 text-red-600 flex items-center justify-center transition-colors duration-200 ml-4"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="bg-white rounded-lg shadow-md p-6">
        <div className="flex justify-between items-center text-lg font-semibold mb-4">
          <span>Total:</span>
          <span className="text-green-600">{formatCurrency(getTotalPrice())}</span>
        </div>

        <div className="flex space-x-3">
          <button
            onClick={clearCart}
            className="flex-1 px-4 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors duration-200"
          >
            Limpar Carrinho
          </button>
          
          <button
            onClick={handleCheckout}
            className="flex-1 bg-green-600 text-white px-4 py-3 rounded-lg hover:bg-green-700 transition-colors duration-200 font-medium"
          >
            Finalizar Pedido
          </button>
        </div>
      </div>
    </div>
  );
};

export default CartView;