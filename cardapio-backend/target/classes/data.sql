-- Inserir usuários padrão (senhas são 'password123' criptografadas com BCrypt)
INSERT INTO usuarios (nome, email, senha, role) VALUES 
('Administrador', 'admin@cardapio.com', '$2a$10$frIGDu4AReMjG1Rnol9b6uQcZnN6RUJ67/eMyIjY1.JBDWdaGIR5K', 'ADMIN'),
('Cozinha Principal', 'cozinha@cardapio.com', '$2a$10$frIGDu4AReMjG1Rnol9b6uQcZnN6RUJ67/eMyIjY1.JBDWdaGIR5K', 'COZINHA'),
('Garçom João', 'garcom@cardapio.com', '$2a$10$frIGDu4AReMjG1Rnol9b6uQcZnN6RUJ67/eMyIjY1.JBDWdaGIR5K', 'GARCOM'),
('Cliente Teste', 'cliente@cardapio.com', '$2a$10$frIGDu4AReMjG1Rnol9b6uQcZnN6RUJ67/eMyIjY1.JBDWdaGIR5K', 'CLIENTE')
ON CONFLICT (email) DO NOTHING;

-- Inserir itens do cardápio
INSERT INTO itens_cardapio (nome, descricao, preco, imagem_url, ativo) VALUES 
('Hambúrguer Clássico', 'Hambúrguer artesanal com carne bovina, alface, tomate, cebola e molho especial', 25.90, 'https://example.com/hamburger.jpg', true),
('Batata Frita', 'Porção de batatas fritas crocantes temperadas com sal', 12.50, 'https://example.com/fries.jpg', true),
('Refrigerante Lata', 'Refrigerante gelado 350ml - Coca-Cola, Pepsi ou Guaraná', 5.00, 'https://example.com/soda.jpg', true),
('Pizza Margherita', 'Pizza tradicional com molho de tomate, mussarela e manjericão', 32.00, 'https://example.com/pizza.jpg', true),
('Salada Caesar', 'Salada com alface americana, croutons, parmesão e molho caesar', 18.90, 'https://example.com/salad.jpg', true),
('Suco Natural', 'Suco natural de laranja, limão ou maracujá - 400ml', 8.50, 'https://example.com/juice.jpg', true),
('Sanduíche de Frango', 'Sanduíche com peito de frango grelhado, alface e tomate', 22.90, 'https://example.com/chicken.jpg', true),
('Água Mineral', 'Água mineral sem gás 500ml', 3.50, 'https://example.com/water.jpg', true),
('Cerveja Long Neck', 'Cerveja gelada long neck 355ml', 7.50, 'https://example.com/beer.jpg', true),
('Sobremesa do Dia', 'Sobremesa especial preparada pelo chef', 15.00, 'https://example.com/dessert.jpg', true)
ON CONFLICT DO NOTHING;

