import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { ChefHat, Eye, EyeOff } from 'lucide-react';
import LoadingSpinner from '../components/Common/LoadingSpinner';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const { login, loading } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!email || !senha) {
      return;
    }

    const success = await login({ email, senha });
    if (success) {
      navigate('/');
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-orange-50 to-red-50 flex items-center justify-center p-4">
      <div className="max-w-md w-full">
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          <div className="bg-gradient-to-r from-orange-600 to-red-600 px-8 py-12">
            <div className="text-center">
              <div className="mx-auto w-16 h-16 bg-white rounded-full flex items-center justify-center mb-4">
                <ChefHat className="w-8 h-8 text-orange-600" />
              </div>
              <h1 className="text-2xl font-bold text-white mb-2">Sistema Cardápio</h1>
              <p className="text-orange-100">Entre com suas credenciais</p>
            </div>
          </div>
          
          <div className="px-8 py-8">
            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                  Email
                </label>
                <input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-colors duration-200"
                  placeholder="seu@email.com"
                  required
                />
              </div>
              
              <div>
                <label htmlFor="senha" className="block text-sm font-medium text-gray-700 mb-2">
                  Senha
                </label>
                <div className="relative">
                  <input
                    id="senha"
                    type={showPassword ? 'text' : 'password'}
                    value={senha}
                    onChange={(e) => setSenha(e.target.value)}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-colors duration-200 pr-12"
                    placeholder="Sua senha"
                    required
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600"
                  >
                    {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
                  </button>
                </div>
              </div>
              
              <button
                type="submit"
                disabled={loading}
                className="w-full bg-gradient-to-r from-orange-600 to-red-600 text-white py-3 px-4 rounded-lg font-medium hover:from-orange-700 hover:to-red-700 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:ring-offset-2 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
              >
                {loading ? (
                  <LoadingSpinner size="sm" />
                ) : (
                  'Entrar'
                )}
              </button>
            </form>
            
            <div className="mt-6 pt-6 border-t border-gray-200">
              <div className="text-sm text-gray-600">
                <p className="font-medium mb-2">Usuários de teste:</p>
                <div className="space-y-1 text-xs">
                  <p><strong>Admin:</strong> admin@cardapio.com / admin123</p>
                  <p><strong>Cliente:</strong> cliente@cardapio.com / cliente123</p>
                  <p><strong>Cozinha:</strong> cozinha@cardapio.com / cozinha123</p>
                  <p><strong>Garçom:</strong> garcom@cardapio.com / garcom123</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;