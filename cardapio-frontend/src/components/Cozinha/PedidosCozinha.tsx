import React, { useState, useEffect } from 'react';
import { Pedido, PedidoStatus } from '../../types';
import { pedidosApi } from '../../services/api';
import { formatDateTime, formatCurrency } from '../../utils/formatters';
import StatusBadge from '../Common/StatusBadge';
import { Clock, ChefHat } from 'lucide-react';
import LoadingSpinner from '../Common/LoadingSpinner';
import toast from 'react-hot-toast';

const PedidosCozinha: React.FC = () => {
  const [pedidos, setPedidos] = useState<Pedido[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPedidos();
    // Atualizar a cada 30 segundos
    const interval = setInterval(loadPedidos, 30000);
    return () => clearInterval(interval);
  }, []);

  const loadPedidos = async () => {
    try {
      const data = await pedidosApi.getByCozinha();
      setPedidos(data.sort((a, b) => new Date(a.dataHora).getTime() - new Date(b.dataHora).getTime()));
    } catch (error) {
      console.error('Erro ao carregar pedidos:', error);
    } finally {
      setLoading(false);
    }
  };

  const updateStatus = async (pedidoId: string, novoStatus: PedidoStatus) => {
    try {
      await pedidosApi.updateStatus(pedidoId, novoStatus);
      toast.success('Status atualizado com sucesso!');
      loadPedidos();
    } catch (error) {
      console.error('Erro ao atualizar status:', error);
    }
  };

  const getStatusActions = (status: PedidoStatus) => {
    switch (status) {
      case PedidoStatus.CONFIRMADO:
        return [{ label: 'Iniciar Preparo', status: PedidoStatus.EM_PREPARO, color: 'bg-orange-600' }];
      case PedidoStatus.EM_PREPARO:
        return [{ label: 'Marcar como Pronto', status: PedidoStatus.PRONTO, color: 'bg-purple-600' }];
      default:
        return [];
    }
  };

  const pedidosAtivos = pedidos.filter(p => 
    [PedidoStatus.CONFIRMADO, PedidoStatus.EM_PREPARO].includes(p.status)
  );

  const pedidosProntos = pedidos.filter(p => p.status === PedidoStatus.PRONTO);

  if (loading) {
    return <LoadingSpinner className="h-64" />;
  }

  const PedidoCard: React.FC<{ pedido: Pedido }> = ({ pedido }) => (
    <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow duration-200">
      <div className="flex justify-between items-start mb-4">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            Pedido #{pedido.id.slice(-6)}
          </h3>
          <p className="text-sm text-gray-600">{formatDateTime(pedido.dataHora)}</p>
          {pedido.mesa && (
            <p className="text-sm text-gray-600">Mesa: {pedido.mesa}</p>
          )}
        </div>
        <StatusBadge status={pedido.status} />
      </div>

      <div className="space-y-2 mb-4">
        {pedido.itens.map((item, index) => (
          <div key={index} className="flex justify-between items-center text-sm">
            <span>{item.quantidade}x {item.itemNome}</span>
            <span className="font-medium">{formatCurrency(item.precoUnitario * item.quantidade)}</span>
          </div>
        ))}
      </div>

      {pedido.observacoes && (
        <div className="mb-4 p-3 bg-yellow-50 border border-yellow-200 rounded-md">
          <p className="text-sm text-yellow-800">
            <strong>Observações:</strong> {pedido.observacoes}
          </p>
        </div>
      )}

      <div className="flex justify-between items-center mb-4">
        <span className="font-semibold">Total: {formatCurrency(pedido.valorTotal)}</span>
        {pedido.clienteNome && (
          <span className="text-sm text-gray-600">Cliente: {pedido.clienteNome}</span>
        )}
      </div>

      <div className="flex space-x-2">
        {getStatusActions(pedido.status).map((action, index) => (
          <button
            key={index}
            onClick={() => updateStatus(pedido.id, action.status)}
            className={`flex-1 ${action.color} text-white px-4 py-2 rounded-lg hover:opacity-90 transition-opacity duration-200 text-sm font-medium`}
          >
            {action.label}
          </button>
        ))}
      </div>
    </div>
  );

  return (
    <div className="space-y-6">
      <h2 className="text-2xl font-bold text-gray-900 flex items-center space-x-2">
        <ChefHat className="w-6 h-6" />
        <span>Pedidos da Cozinha</span>
      </h2>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div>
          <h3 className="text-xl font-semibold text-gray-900 mb-4 flex items-center space-x-2">
            <Clock className="w-5 h-5 text-orange-600" />
            <span>Pedidos em Andamento ({pedidosAtivos.length})</span>
          </h3>
          <div className="space-y-4">
            {pedidosAtivos.length === 0 ? (
              <p className="text-gray-500 text-center py-8">Nenhum pedido em andamento.</p>
            ) : (
              pedidosAtivos.map(pedido => (
                <PedidoCard key={pedido.id} pedido={pedido} />
              ))
            )}
          </div>
        </div>

        <div>
          <h3 className="text-xl font-semibold text-gray-900 mb-4 flex items-center space-x-2">
            <ChefHat className="w-5 h-5 text-purple-600" />
            <span>Pedidos Prontos ({pedidosProntos.length})</span>
          </h3>
          <div className="space-y-4">
            {pedidosProntos.length === 0 ? (
              <p className="text-gray-500 text-center py-8">Nenhum pedido pronto.</p>
            ) : (
              pedidosProntos.map(pedido => (
                <PedidoCard key={pedido.id} pedido={pedido} />
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PedidosCozinha;