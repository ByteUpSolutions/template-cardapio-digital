import React, { useState, useEffect } from 'react';
import { ItemCardapio } from '../../types';
import { menuApi } from '../../services/api';
import { Plus, Edit, Trash2, Search } from 'lucide-react';
import { formatCurrency } from '../../utils/formatters';
import Modal from '../Common/Modal';
import LoadingSpinner from '../Common/LoadingSpinner';
import toast from 'react-hot-toast';

const CardapioManagement: React.FC = () => {
  const [itens, setItens] = useState<ItemCardapio[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<ItemCardapio | null>(null);
  const [formData, setFormData] = useState({
    nome: '',
    descricao: '',
    preco: '',
    categoria: '',
    disponivel: true,
    imagemUrl: '',
    tempoPreparoMinutos: ''
  });

  useEffect(() => {
    loadItens();
  }, []);

  const loadItens = async () => {
    try {
      setLoading(true);
      const data = await menuApi.getItens();
      setItens(data);
    } catch (error) {
      console.error('Erro ao carregar itens:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (item?: ItemCardapio) => {
    if (item) {
      setEditingItem(item);
      setFormData({
        nome: item.nome,
        descricao: item.descricao,
        preco: item.preco.toString(),
        categoria: item.categoria,
        disponivel: item.disponivel,
        imagemUrl: item.imagemUrl || '',
        tempoPreparoMinutos: item.tempoPreparoMinutos?.toString() || ''
      });
    } else {
      setEditingItem(null);
      setFormData({
        nome: '',
        descricao: '',
        preco: '',
        categoria: '',
        disponivel: true,
        imagemUrl: '',
        tempoPreparoMinutos: ''
      });
    }
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const itemData = {
        nome: formData.nome,
        descricao: formData.descricao,
        preco: parseFloat(formData.preco),
        categoria: formData.categoria,
        disponivel: formData.disponivel,
        imagemUrl: formData.imagemUrl || undefined,
        tempoPreparoMinutos: formData.tempoPreparoMinutos ? parseInt(formData.tempoPreparoMinutos) : undefined
      };

      if (editingItem) {
        await menuApi.updateItem(editingItem.id, itemData);
        toast.success('Item atualizado com sucesso!');
      } else {
        await menuApi.createItem(itemData);
        toast.success('Item criado com sucesso!');
      }
      
      setIsModalOpen(false);
      loadItens();
    } catch (error) {
      console.error('Erro ao salvar item:', error);
    }
  };

  const handleDelete = async (id: string) => {
    if (window.confirm('Tem certeza que deseja excluir este item?')) {
      try {
        await menuApi.deleteItem(id);
        toast.success('Item excluído com sucesso!');
        loadItens();
      } catch (error) {
        console.error('Erro ao excluir item:', error);
      }
    }
  };

  const filteredItens = itens.filter(item =>
    item.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
    item.categoria.toLowerCase().includes(searchTerm.toLowerCase())
  );

  if (loading) {
    return <LoadingSpinner className="h-64" />;
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Gerenciar Cardápio</h2>
        <button
          onClick={() => handleOpenModal()}
          className="flex items-center space-x-2 bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition-colors duration-200"
        >
          <Plus className="w-5 h-5" />
          <span>Novo Item</span>
        </button>
      </div>

      <div className="relative">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
        <input
          type="text"
          placeholder="Buscar por nome ou categoria..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
        />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredItens.map((item) => (
          <div key={item.id} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-200">
            {item.imagemUrl && (
              <img
                src={item.imagemUrl}
                alt={item.nome}
                className="w-full h-48 object-cover"
              />
            )}
            <div className="p-4">
              <div className="flex justify-between items-start mb-2">
                <h3 className="text-lg font-semibold text-gray-900">{item.nome}</h3>
                <div className="flex space-x-1">
                  <button
                    onClick={() => handleOpenModal(item)}
                    className="p-2 text-blue-600 hover:bg-blue-50 rounded-full transition-colors duration-200"
                  >
                    <Edit className="w-4 h-4" />
                  </button>
                  <button
                    onClick={() => handleDelete(item.id)}
                    className="p-2 text-red-600 hover:bg-red-50 rounded-full transition-colors duration-200"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
              
              <p className="text-gray-600 text-sm mb-3">{item.descricao}</p>
              
              <div className="flex justify-between items-center mb-2">
                <span className="text-lg font-bold text-green-600">
                  {formatCurrency(item.preco)}
                </span>
                <span className="text-sm text-gray-500 bg-gray-100 px-2 py-1 rounded">
                  {item.categoria}
                </span>
              </div>
              
              <div className="flex justify-between items-center">
                <span className={`text-xs px-2 py-1 rounded-full ${
                  item.disponivel 
                    ? 'bg-green-100 text-green-800' 
                    : 'bg-red-100 text-red-800'
                }`}>
                  {item.disponivel ? 'Disponível' : 'Indisponível'}
                </span>
                
                {item.tempoPreparoMinutos && (
                  <span className="text-xs text-gray-500">
                    {item.tempoPreparoMinutos} min
                  </span>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingItem ? 'Editar Item' : 'Novo Item'}
        className="max-w-lg"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Nome
            </label>
            <input
              type="text"
              value={formData.nome}
              onChange={(e) => setFormData({...formData, nome: e.target.value})}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Descrição
            </label>
            <textarea
              value={formData.descricao}
              onChange={(e) => setFormData({...formData, descricao: e.target.value})}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
              rows={3}
              required
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Preço
              </label>
              <input
                type="number"
                step="0.01"
                value={formData.preco}
                onChange={(e) => setFormData({...formData, preco: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Categoria
              </label>
              <select
                value={formData.categoria}
                onChange={(e) => setFormData({...formData, categoria: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
                required
              >
                <option value="">Selecione</option>
                <option value="Entrada">Entrada</option>
                <option value="Prato Principal">Prato Principal</option>
                <option value="Sobremesa">Sobremesa</option>
                <option value="Bebida">Bebida</option>
                <option value="Lanche">Lanche</option>
              </select>
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              URL da Imagem
            </label>
            <input
              type="url"
              value={formData.imagemUrl}
              onChange={(e) => setFormData({...formData, imagemUrl: e.target.value})}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
              placeholder="https://exemplo.com/imagem.jpg"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tempo de Preparo (minutos)
            </label>
            <input
              type="number"
              value={formData.tempoPreparoMinutos}
              onChange={(e) => setFormData({...formData, tempoPreparoMinutos: e.target.value})}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
              placeholder="Ex: 15"
            />
          </div>

          <div className="flex items-center">
            <input
              type="checkbox"
              id="disponivel"
              checked={formData.disponivel}
              onChange={(e) => setFormData({...formData, disponivel: e.target.checked})}
              className="w-4 h-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded"
            />
            <label htmlFor="disponivel" className="ml-2 text-sm text-gray-700">
              Item disponível
            </label>
          </div>

          <div className="flex space-x-3 pt-4">
            <button
              type="button"
              onClick={() => setIsModalOpen(false)}
              className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors duration-200"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="flex-1 bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 transition-colors duration-200"
            >
              {editingItem ? 'Atualizar' : 'Criar'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default CardapioManagement;