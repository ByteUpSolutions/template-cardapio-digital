import React, { useState, useEffect } from 'react';
import { ItemCardapio } from '../../types';
import { menuApi } from '../../services/api';
import { useCart } from '../../contexts/CartContext';
import { Plus, Minus, ShoppingCart } from 'lucide-react';
import { formatCurrency } from '../../utils/formatters';
import LoadingSpinner from '../Common/LoadingSpinner';
import toast from 'react-hot-toast';

const CardapioView: React.FC = () => {
  const [itens, setItens] = useState<ItemCardapio[]>([]);
  const [loading, setLoading] = useState(true);
  const [categoriaFiltro, setCategoriaFiltro] = useState<string>('');
  const [quantities, setQuantities] = useState<{ [key: string]: number }>({});
  const [observacoes, setObservacoes] = useState<{ [key: string]: string }>({});
  const { addItem, getTotalItems } = useCart();

  useEffect(() => {
    loadItens();
  }, []);

  const loadItens = async () => {
    try {
      setLoading(true);
      const data = await menuApi.getItens();
      const itensDisponiveis = data.filter(item => item.disponivel);
      setItens(itensDisponiveis);
    } catch (error) {
      console.error('Erro ao carregar cardápio:', error);
    } finally {
      setLoading(false);
    }
  };

  const categorias = [...new Set(itens.map(item => item.categoria))];
  
  const itensFiltrados = categoriaFiltro 
    ? itens.filter(item => item.categoria === categoriaFiltro)
    : itens;

  const updateQuantity = (itemId: string, newQuantity: number) => {
    setQuantities(prev => ({
      ...prev,
      [itemId]: Math.max(0, newQuantity)
    }));
  };

  const handleAddToCart = (item: ItemCardapio) => {
    const quantidade = quantities[item.id] || 1;
    const obs = observacoes[item.id] || '';
    
    addItem(item, quantidade, obs);
    toast.success(`${quantidade}x ${item.nome} adicionado ao carrinho!`);
    
    // Reset form
    setQuantities(prev => ({ ...prev, [item.id]: 1 }));
    setObservacoes(prev => ({ ...prev, [item.id]: '' }));
  };

  if (loading) {
    return <LoadingSpinner className="h-64" />;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Cardápio</h2>
        <div className="flex items-center space-x-2 bg-green-600 text-white px-4 py-2 rounded-lg">
          <ShoppingCart className="w-5 h-5" />
          <span className="font-medium">{getTotalItems()} itens</span>
        </div>
      </div>

      {/* Filtro por categoria */}
      <div className="flex flex-wrap gap-2">
        <button
          onClick={() => setCategoriaFiltro('')}
          className={`px-4 py-2 rounded-full transition-colors duration-200 ${
            !categoriaFiltro 
              ? 'bg-green-600 text-white' 
              : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
          }`}
        >
          Todas
        </button>
        {categorias.map(categoria => (
          <button
            key={categoria}
            onClick={() => setCategoriaFiltro(categoria)}
            className={`px-4 py-2 rounded-full transition-colors duration-200 ${
              categoriaFiltro === categoria 
                ? 'bg-green-600 text-white' 
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            {categoria}
          </button>
        ))}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {itensFiltrados.map((item) => (
          <div key={item.id} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-200">
            {item.imagemUrl && (
              <img
                src={item.imagemUrl}
                alt={item.nome}
                className="w-full h-48 object-cover"
              />
            )}
            <div className="p-6">
              <div className="flex justify-between items-start mb-2">
                <h3 className="text-lg font-semibold text-gray-900">{item.nome}</h3>
                <span className="text-lg font-bold text-green-600">
                  {formatCurrency(item.preco)}
                </span>
              </div>
              
              <p className="text-gray-600 text-sm mb-4">{item.descricao}</p>
              
              <div className="flex items-center justify-between mb-4">
                <span className="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">
                  {item.categoria}
                </span>
                {item.tempoPreparoMinutos && (
                  <span className="text-xs text-gray-500">
                    ~{item.tempoPreparoMinutos} min
                  </span>
                )}
              </div>

              <div className="space-y-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Observações
                  </label>
                  <input
                    type="text"
                    placeholder="Ex: sem cebola, ponto da carne..."
                    value={observacoes[item.id] || ''}
                    onChange={(e) => setObservacoes(prev => ({
                      ...prev,
                      [item.id]: e.target.value
                    }))}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-green-500 focus:border-green-500 text-sm"
                  />
                </div>

                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-3">
                    <button
                      onClick={() => updateQuantity(item.id, (quantities[item.id] || 1) - 1)}
                      className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors duration-200"
                    >
                      <Minus className="w-4 h-4" />
                    </button>
                    <span className="font-medium w-8 text-center">
                      {quantities[item.id] || 1}
                    </span>
                    <button
                      onClick={() => updateQuantity(item.id, (quantities[item.id] || 1) + 1)}
                      className="w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors duration-200"
                    >
                      <Plus className="w-4 h-4" />
                    </button>
                  </div>

                  <button
                    onClick={() => handleAddToCart(item)}
                    className="flex items-center space-x-2 bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors duration-200"
                  >
                    <ShoppingCart className="w-4 h-4" />
                    <span>Adicionar</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {itensFiltrados.length === 0 && (
        <div className="text-center py-12">
          <p className="text-gray-500">Nenhum item encontrado.</p>
        </div>
      )}
    </div>
  );
};

export default CardapioView;