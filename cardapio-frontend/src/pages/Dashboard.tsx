import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { UserType, Pedido, ItemCardapio } from '../types';
import { pedidosApi, menuApi } from '../services/api';
import { 
  BarChart3, 
  ShoppingCart, 
  Menu, 
  Users, 
  TrendingUp,
  Clock,
  CheckCircle,
  XCircle
} from 'lucide-react';
import { formatCurrency } from '../utils/formatters';
import LoadingSpinner from '../components/Common/LoadingSpinner';

const Dashboard: React.FC = () => {
  const { userType } = useAuth();
  const [pedidos, setPedidos] = useState<Pedido[]>([]);
  const [itens, setItens] = useState<ItemCardapio[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
  try {
    setLoading(true);
    // A chamada para menuApi.getItens() também precisa ser corrigida
    const [pedidosResponse, itensData] = await Promise.all([
      userType === UserType.ADMIN ? pedidosApi.getAll() : pedidosApi.getByCozinha(),
      menuApi.getItens()
    ]);

    // CORREÇÃO AQUI:
    // Se a resposta for paginada (tem 'content'), use o conteúdo. Senão, use a resposta diretamente.
    const pedidosData = 'content' in pedidosResponse ? pedidosResponse.content : pedidosResponse;

    setPedidos(pedidosData);
    setItens(itensData);
  } catch (error) {
    console.error('Erro ao carregar dados:', error);
  } finally {
    setLoading(false);
  }
};

  if (loading) {
    return <LoadingSpinner className="h-64" />;
  }

  const hoje = new Date().toISOString().split('T')[0];
  const pedidosHoje = pedidos.filter(p => p.dataHora.startsWith(hoje));
  
  const stats = {
    totalPedidos: pedidosHoje.length,
    pedidosPendentes: pedidos.filter(p => p.status === 'PENDENTE').length,
    pedidosEmAndamento: pedidos.filter(p => ['CONFIRMADO', 'EM_PREPARO'].includes(p.status)).length,
    pedidosEntregues: pedidosHoje.filter(p => p.status === 'ENTREGUE').length,
    receitaHoje: pedidosHoje
      .filter(p => p.status === 'ENTREGUE')
      .reduce((sum, p) => sum + p.valorTotal, 0),
    itensDisponiveis: itens.filter(i => i.disponivel).length,
    totalItens: itens.length
  };

  const StatCard: React.FC<{
    title: string;
    value: string | number;
    icon: React.ReactNode;
    color: string;
    subtitle?: string;
  }> = ({ title, value, icon, color, subtitle }) => (
    <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow duration-200">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-2xl font-bold text-gray-900">{value}</p>
          {subtitle && <p className="text-xs text-gray-500 mt-1">{subtitle}</p>}
        </div>
        <div className={`p-3 rounded-full ${color}`}>
          {icon}
        </div>
      </div>
    </div>
  );

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <div className="text-sm text-gray-500">
          Última atualização: {new Date().toLocaleTimeString()}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Pedidos Hoje"
          value={stats.totalPedidos}
          icon={<ShoppingCart className="w-6 h-6 text-white" />}
          color="bg-blue-500"
          subtitle="Total do dia"
        />
        
        <StatCard
          title="Em Andamento"
          value={stats.pedidosEmAndamento}
          icon={<Clock className="w-6 h-6 text-white" />}
          color="bg-orange-500"
          subtitle="Confirmados + Em preparo"
        />
        
        <StatCard
          title="Entregues"
          value={stats.pedidosEntregues}
          icon={<CheckCircle className="w-6 h-6 text-white" />}
          color="bg-green-500"
          subtitle="Concluídos hoje"
        />
        
        <StatCard
          title="Receita Hoje"
          value={formatCurrency(stats.receitaHoje)}
          icon={<TrendingUp className="w-6 h-6 text-white" />}
          color="bg-purple-500"
          subtitle="Vendas do dia"
        />
      </div>

      {userType === UserType.ADMIN && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <StatCard
            title="Itens do Cardápio"
            value={`${stats.itensDisponiveis}/${stats.totalItens}`}
            icon={<Menu className="w-6 h-6 text-white" />}
            color="bg-indigo-500"
            subtitle="Disponíveis / Total"
          />
          
          <StatCard
            title="Pedidos Pendentes"
            value={stats.pedidosPendentes}
            icon={<XCircle className="w-6 h-6 text-white" />}
            color="bg-red-500"
            subtitle="Aguardando confirmação"
          />
        </div>
      )}

      {/* Últimos pedidos */}
      <div className="bg-white rounded-lg shadow-md">
        <div className="px-6 py-4 border-b">
          <h3 className="text-lg font-semibold text-gray-900">Últimos Pedidos</h3>
        </div>
        <div className="divide-y">
          {pedidos.slice(0, 5).map((pedido) => (
            <div key={pedido.id} className="px-6 py-4 hover:bg-gray-50 transition-colors duration-200">
              <div className="flex items-center justify-between">
                <div>
                  <p className="font-medium text-gray-900">
                    Pedido #{pedido.id.slice(-6)}
                  </p>
                  <p className="text-sm text-gray-600">
                    {pedido.clienteNome} • {formatDateTime(pedido.dataHora)}
                  </p>
                </div>
                <div className="flex items-center space-x-3">
                  <span className="font-semibold text-gray-900">
                    {formatCurrency(pedido.valorTotal)}
                  </span>
                  <StatusBadge status={pedido.status} />
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;