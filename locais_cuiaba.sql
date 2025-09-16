TRUNCATE TABLE local_tags;
TRUNCATE TABLE locais;


-- LOCAIS DE CUIABÁ POR CATEGORIA/TAG

-- =================== CULTURA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Casa do Artesão', 'Centro de artesanato local com peças típicas de Mato Grosso', 'Praça do Arsenal, Centro Histórico, Cuiabá', '(65) 3624-9060', '08:00-18:00', 0.0, 4.3, true, false, false, 60, true, -15.601481, -56.097889),
('Museu da Pedra', 'Museu dedicado à geologia e mineralogia de Mato Grosso', 'Rua Galdino Pimentel, 195, Centro', '(65) 3624-7456', '08:00-12:00, 13:30-17:30', 10.0, 4.1, true, false, false, 90, true, -15.596311, -56.098056),
('Centro Geodésico da América do Sul', 'Marco geodésico e centro cultural', 'BR-163, Km 0, Várzea Grande', '(65) 3688-8000', '08:00-17:00', 5.0, 4.0, true, false, false, 45, true, -15.646929, -56.142622),
('Teatro de Arena', 'Teatro ao ar livre para apresentações culturais', 'Centro de Eventos do Pantanal, Várzea Grande', '(65) 3688-8100', 'Conforme programação', 25.0, 4.2, true, true, true, 120, true, -15.647258, -56.127889),
('Biblioteca Estevão de Mendonça', 'Principal biblioteca pública com acervo histórico', 'Rua Antonio Maria Coelho, Centro', '(65) 3624-7500', '07:00-19:00', 0.0, 4.0, true, false, false, 75, true, -15.595833, -56.096944);

-- =================== NATUREZA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Arena Pantanal', 'Estádio moderno cercado por área verde', 'Av. Agrícola Paes de Barros, Verdão', '(65) 3617-3400', 'Conforme eventos', 50.0, 4.5, true, true, false, 180, true, -15.603333, -56.122778),
('Parque Tia Nair', 'Parque urbano com trilhas e área verde', 'Rua Desembargador Milton Figueiredo, Quilombo', '(65) 3617-1500', '06:00-18:00', 0.0, 4.2, true, false, false, 90, true, -15.580556, -56.094167),
('Parque Zé Bolo Flô', 'Parque ecológico com lago e trilhas', 'Av. Miguel Sutil, Consil', '(65) 3617-1600', '06:00-18:00', 0.0, 4.3, true, false, false, 120, true, -15.570833, -56.086389),
('Jardim Botânico de Cuiabá', 'Área de preservação com diversas espécies nativas', 'Av. Miguel Sutil, 8000, Ribeirão da Ponte', '(65) 3617-1700', '08:00-17:00', 8.0, 4.4, true, false, false, 150, true, -15.555278, -56.058611),
('Parque da Família', 'Grande parque com lagos artificiais e ciclovias', 'Av. Dom Orlando Chaves, Centro Norte', '(65) 3617-1800', '05:00-22:00', 0.0, 4.6, true, true, false, 120, true, -15.578056, -56.078611);

-- =================== ESPORTE ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Estádio Governador José Fragelli', 'Estádio tradicional do Verdão', 'Av. Rubens de Mendonça, Bosque da Saúde', '(65) 3617-3500', 'Conforme jogos', 30.0, 4.0, true, true, false, 150, true, -15.571389, -56.065833),
('Centro de Formação Olímpica', 'Complexo esportivo com diversas modalidades', 'Av. Miguel Sutil, 8001, Ribeirão da Ponte', '(65) 3617-4000', '06:00-22:00', 15.0, 4.3, true, true, false, 120, true, -15.556111, -56.059444),
('Ginásio Aecim Tocantins', 'Ginásio poliesportivo para basquete e vôlei', 'Av. Tenente Coronel Duarte, Centro Norte', '(65) 3617-4100', '08:00-22:00', 20.0, 4.1, true, true, false, 90, true, -15.575000, -56.075556),
('Kartódromo de Cuiabá', 'Pista de kart profissional', 'Rodovia Emanuel Pinheiro, CPA IV', '(65) 3617-5000', '09:00-18:00', 80.0, 4.4, false, false, false, 45, true, -15.650000, -56.050000),
('Aqua Sports', 'Centro aquático com piscinas para competição', 'Av. Fernando Correa da Costa, Coxipó', '(65) 3615-8000', '06:00-22:00', 25.0, 4.2, true, true, false, 120, true, -15.615556, -56.069722);

-- =================== NOTURNO ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Sky Bar Cuiabá', 'Bar no rooftop com vista panorâmica da cidade', 'Av. Historiador Rubens de Mendonça, Bosque', '(65) 99999-1001', '18:00-02:00', 120.0, 4.5, false, true, true, 180, true, -15.570278, -56.065278),
('Choperia do Gordo', 'Bar tradicional com chopp artesanal', 'Rua Antônio Maria Coelho, Centro', '(65) 3624-5555', '17:00-01:00', 60.0, 4.3, false, true, true, 150, true, -15.596389, -56.097222),
('Dublin Irish Pub', 'Pub irlandês com música ao vivo', 'Av. Carmindo de Campo, Santana', '(65) 99999-1002', '18:00-02:00', 80.0, 4.4, false, true, true, 180, true, -15.565556, -56.058333),
('Pinguim Bar', 'Bar clássico cuiabano com ambiente descontraído', 'Rua Joaquim Murtinho, Centro', '(65) 3624-6666', '16:00-00:00', 50.0, 4.2, false, true, true, 120, true, -15.597500, -56.098611),
('Matrix Club', 'Casa noturna com DJs e pista de dança', 'Av. Miguel Sutil, Duque de Caxias', '(65) 99999-1003', '22:00-05:00', 100.0, 4.1, false, false, true, 240, true, -15.555833, -56.086944);

-- =================== HISTÓRIA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Casa do Barão de Melgaço', 'Museu histórico em casarão colonial', 'Rua Galdino Pimentel, Centro Histórico', '(65) 3624-9070', '08:00-17:00', 8.0, 4.3, true, false, false, 75, true, -15.595556, -56.098333),
('Museu de Mato Grosso', 'Acervo histórico sobre a formação do estado', 'Praça da República, Centro', '(65) 3624-7460', '08:00-17:30', 5.0, 4.2, true, false, false, 90, true, -15.601667, -56.097500),
('Igreja do Largo de Santana', 'Igreja histórica do século XVIII', 'Largo de Santana, Centro', '(65) 3624-8000', '07:00-12:00, 17:00-20:00', 0.0, 4.4, true, false, false, 30, true, -15.601111, -56.096667),
('Museu do Rio Aquário', 'Museu sobre a história dos rios da região', 'Rua Barão de Melgaço, Centro', '(65) 3624-7470', '09:00-17:00', 12.0, 4.1, true, false, false, 100, true, -15.597778, -56.099444),
('Fortaleza de Junqueirópolis', 'Fortificação histórica preservada', 'Distrito de Junqueirópolis, Cuiabá', '(65) 3624-8100', '08:00-16:00', 6.0, 4.0, true, false, false, 60, true, -15.520000, -56.150000);

-- =================== BAR ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Empório do Chopp', 'Bar especializado em cervejas artesanais', 'Av. Carmindo de Campos, Santana', '(65) 99999-2001', '17:00-01:00', 70.0, 4.4, false, true, true, 150, true, -15.565278, -56.058056),
('Bar do Mineiro', 'Bar tradicional com petiscos mineiros', 'Rua Presidente Marques, Lixeira', '(65) 3624-7777', '16:00-00:00', 45.0, 4.2, false, true, true, 120, true, -15.605556, -56.105278),
('Boteco da Esquina', 'Boteco com ambiente familiar e música ao vivo', 'Rua 24 de Outubro, Bandeirantes', '(65) 99999-2002', '18:00-02:00', 55.0, 4.3, false, true, true, 180, true, -15.625000, -56.092500),
('Wine House Cuiabá', 'Bar de vinhos com ambiente sofisticado', 'Av. Miguel Sutil, Jardim Leblon', '(65) 99999-2003', '18:30-01:00', 150.0, 4.6, false, true, true, 180, true, -15.555000, -56.085833),
('Brahma Center', 'Bar com tema da cerveja Brahma', 'Av. Getúlio Vargas, Centro Norte', '(65) 99999-2004', '17:00-01:30', 65.0, 4.1, false, true, true, 150, true, -15.578333, -56.078889);

-- =================== MUSEU ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Museu da História Natural', 'Museu com fósseis e espécimes da região', 'UFMT Campus, Boa Esperança', '(65) 3615-8400', '08:00-17:00', 5.0, 4.3, true, false, false, 90, true, -15.620833, -56.068611),
('Museu de Pedras Ramis Bucair', 'Coleção de pedras preciosas e minerais', 'Rua Galdino Pimentel, Centro', '(65) 3624-9080', '08:00-18:00', 8.0, 4.2, true, false, false, 60, true, -15.595278, -56.098056),
('Museu da Imagem e do Som', 'Acervo audiovisual da cultura matogrossense', 'Rua Barão de Melgaço, Centro', '(65) 3624-7480', '08:00-17:00', 0.0, 4.1, true, false, false, 75, true, -15.597500, -56.099167),
('Memorial da América Latina', 'Espaço dedicado à integração latino-americana', 'Centro Político Administrativo', '(65) 3617-9000', '09:00-18:00', 0.0, 4.0, true, false, false, 120, true, -15.610000, -56.097500),
('Museu de Arqueologia', 'Artefatos arqueológicos do Pantanal', 'Av. Fernando Corrêa da Costa, UFMT', '(65) 3615-8500', '08:00-17:00', 3.0, 4.2, true, false, false, 100, true, -15.618889, -56.067222);

-- =================== ANIMAL ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Zoológico de Cuiabá', 'Zoológico com fauna pantaneira e brasileira', 'Av. Fernando Corrêa da Costa, Coxipó', '(65) 3617-6000', '08:00-17:00', 15.0, 4.4, true, true, false, 180, true, -15.615278, -56.069444),
('Aquário de Cuiabá', 'Aquário com peixes da região amazônica', 'Shopping Pantanal, Duque de Caxias', '(65) 99999-3001', '10:00-22:00', 25.0, 4.3, true, true, false, 120, true, -15.555556, -56.086667),
('Criadouro de Jacarés', 'Criação conservacionista de jacarés', 'Estrada do Coxipó, Km 12', '(65) 99999-3002', '08:00-16:00', 20.0, 4.2, true, false, false, 90, true, -15.650000, -56.080000),
('Fazenda São João', 'Fazenda pedagógica com animais domésticos', 'Rodovia Cuiabá-Rosário, Km 18', '(65) 99999-3003', '08:00-17:00', 30.0, 4.5, true, true, false, 240, true, -15.450000, -56.050000),
('Centro de Reabilitação de Animais Silvestres', 'CRAS para recuperação da fauna', 'Av. Fernando Corrêa da Costa, UFMT', '(65) 3615-8600', '08:00-16:00', 0.0, 4.1, true, false, false, 75, true, -15.620000, -56.068333);

-- =================== ARTE ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Galeria Metamorfose', 'Galeria de arte contemporânea local', 'Rua Marechal Deodoro, Centro', '(65) 99999-4001', '09:00-18:00', 0.0, 4.3, true, false, false, 60, true, -15.596667, -56.097778),
('Centro Cultural da UFMT', 'Espaço cultural universitário com exposições', 'UFMT Campus, Boa Esperança', '(65) 3615-8700', '08:00-22:00', 0.0, 4.2, true, true, true, 90, true, -15.620556, -56.068889),
('Atelier Cores do Pantanal', 'Ateliê de pintores regionais', 'Rua Isaac Povoas, Centro', '(65) 99999-4002', '09:00-17:00', 0.0, 4.4, true, false, false, 45, true, -15.595000, -56.096111),
('Casa de Artes de Cuiabá', 'Centro de formação e exposição artística', 'Av. Getúlio Vargas, Lixeira', '(65) 3624-9090', '08:00-18:00', 5.0, 4.1, true, false, false, 75, true, -15.605000, -56.104444),
('Espaço Cultural Palácio da Instrução', 'Palácio histórico com exposições de arte', 'Praça da República, Centro', '(65) 3624-9100', '08:00-18:00', 0.0, 4.5, true, false, false, 90, true, -15.601944, -56.097222);

-- =================== SHOPPING ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Shopping Pantanal', 'Maior shopping center de Cuiabá', 'Av. Fernando Corrêa da Costa, 3255', '(65) 3025-2000', '10:00-22:00', 80.0, 4.4, true, true, false, 180, true, -15.555833, -56.086389),
('Shopping Três Américas', 'Shopping com lojas e praça de alimentação', 'Av. Três Américas, Alvorada', '(65) 3363-7000', '10:00-22:00', 70.0, 4.2, true, true, false, 150, true, -15.575278, -56.062778),
('Shopping Cuiabá', 'Shopping tradicional do centro expandido', 'Rua Barão de Melgaço, Centro Norte', '(65) 3025-3000', '10:00-22:00', 75.0, 4.1, true, true, false, 160, true, -15.578056, -56.078333),
('Goiabeiras Shopping', 'Shopping regional com cinema e restaurantes', 'Av. Fernando Corrêa da Costa, Goiabeiras', '(65) 3025-4000', '10:00-22:00', 65.0, 4.3, true, true, false, 140, true, -15.600000, -56.070000),
('Arsenal Shopping', 'Shopping no centro histórico', 'Rua Voluntários da Pátria, Arsenal', '(65) 3025-5000', '10:00-22:00', 60.0, 4.0, true, true, false, 120, true, -15.602500, -56.100000);

-- =================== FAMÍLIA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Parque Mãe Bonifácia', 'Grande parque urbano com playground e trilhas', 'Av. Miguel Sutil, CPA', '(65) 3617-1900', '05:00-19:00', 0.0, 4.6, true, false, false, 120, true, -15.570000, -56.085556),
('Sesi Lab', 'Centro de ciências interativo para crianças', 'Av. Historiador Rubens de Mendonça, Jd. Leblon', '(65) 3611-1000', '08:00-17:00', 20.0, 4.5, true, true, false, 150, true, -15.570556, -56.065000),
('Parque Aquático Águas do Cerrado', 'Parque aquático com piscinas e tobogãs', 'Rodovia dos Imigrantes, Km 8', '(65) 99999-5001', '09:00-17:00', 45.0, 4.4, true, true, false, 240, true, -15.520000, -56.100000),
('Fazenda Educativa Natureza Viva', 'Fazenda para contato com animais e natureza', 'Chapada dos Guimarães, Km 25', '(65) 99999-5002', '08:00-17:00', 35.0, 4.3, true, true, false, 300, true, -15.400000, -55.900000),
('Espaço Kids Cuiabá', 'Área de recreação infantil coberta', 'Shopping Pantanal, Duque de Caxias', '(65) 99999-5003', '10:00-22:00', 25.0, 4.2, true, true, false, 120, true, -15.555556, -56.086667);

-- =================== PARQUE DE DIVERSÕES ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Hopi Hari Cuiabá', 'Parque temático com montanha-russa e brinquedos', 'Av. Fernando Corrêa da Costa, Coxipó', '(65) 99999-6001', '10:00-18:00', 60.0, 4.4, true, true, false, 300, true, -15.615000, -56.070000),
('Magic City', 'Parque de diversões com roda gigante', 'Shopping Três Américas, Alvorada', '(65) 99999-6002', '10:00-22:00', 40.0, 4.2, true, true, false, 180, true, -15.575278, -56.062778),
('Play Park', 'Área de jogos eletrônicos e brinquedos', 'Shopping Pantanal, Duque de Caxias', '(65) 99999-6003', '10:00-22:00', 35.0, 4.3, true, true, false, 120, true, -15.555833, -56.086389),
('Adventure Park', 'Parque de aventuras com tirolesa e arvorismo', 'Parque Tia Nair, Quilombo', '(65) 99999-6004', '08:00-17:00', 50.0, 4.5, true, false, false, 180, true, -15.580556, -56.094167),
('Fun Space', 'Centro de entretenimento com boliche e jogos', 'Av. Miguel Sutil, Jardim Leblon', '(65) 99999-6005', '14:00-23:00', 45.0, 4.1, true, true, true, 150, true, -15.555000, -56.085833);

-- =================== ÁGUA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Balneário Águas Termais', 'Complexo de piscinas com águas termais', 'Estrada da Guia, Km 15', '(65) 99999-7001', '08:00-18:00', 30.0, 4.3, true, true, false, 240, true, -15.500000, -56.050000),
('Clube Náutico de Cuiabá', 'Clube com piscinas e esportes aquáticos', 'Av. Beira Rio, Cidade Verdão', '(65) 3617-8000', '07:00-19:00', 40.0, 4.4, true, true, false, 180, true, -15.600000, -56.120000),
('Parque Aquático Thermas', 'Parque com piscinas termais e recreativas', 'Rodovia Cuiabá-Rosário, Km 20', '(65) 99999-7002', '09:00-17:00', 50.0, 4.5, true, true, false, 300, true, -15.450000, -56.080000),
('Rio Coxipó Mirim', 'Balneário natural no rio', 'Distrito do Coxipó da Ponte', '(65) 99999-7003', '08:00-17:00', 10.0, 4.2, true, false, false, 180, true, -15.650000, -56.050000),
('Aqua Center', 'Centro aquático com piscinas olímpicas', 'Av. Fernando Corrêa da Costa, UFMT', '(65) 3615-8800', '06:00-22:00', 20.0, 4.1, true, true, false, 120, true, -15.618611, -56.067500);

-- =================== CHURRASCO ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Churrascaria Pantanal', 'Tradicional churrascaria com carnes regionais', 'Av. Getúlio Vargas, 1147, Lixeira', '(65) 3624-8800', '11:00-15:00, 18:00-23:00', 65.0, 4.5, true, true, false, 120, true, -15.605278, -56.104722),
('Portal do Pantanal', 'Churrascaria com buffet de saladas', 'Rua Joaquim Murtinho, Centro', '(65) 3624-9900', '11:00-15:00, 18:00-00:00', 70.0, 4.4, true, true, true, 120, true, -15.597222, -56.098889),
('Espeto de Ouro', 'Rodízio de carnes com ambiente familiar', 'Av. Miguel Sutil, Duque de Caxias', '(65) 3025-6000', '11:00-15:00, 18:00-23:00', 75.0, 4.3, true, true, false, 120, true, -15.555556, -56.087222),
('Churrascaria do Gaúcho', 'Especializada em cortes nobres', 'Av. Historiador Rubens de Mendonça, Bosque', '(65) 99999-8001', '11:30-15:00, 18:30-23:30', 90.0, 4.6, true, true, false, 140, true, -15.570278, -56.065556),
('Tradição Gaúcha', 'Churrascaria com shows folclóricos', 'Av. Carmindo de Campos, Santana', '(65) 99999-8002', '11:00-15:00, 18:00-00:00', 80.0, 4.4, true, true, true, 150, true, -15.565833, -56.058611);

-- =================== BRASILEIRA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Restaurante Peixe Pintado', 'Especializado em peixes do Pantanal', 'Rua Barão de Melgaço, Centro', '(65) 3624-7000', '11:00-15:00, 18:00-23:00', 55.0, 4.5, true, true, false, 90, true, -15.597778, -56.099722),
('Casa do Cupim', 'Pratos típicos de Mato Grosso', 'Av. Fernando Corrêa da Costa, Coxipó', '(65) 3025-7000', '11:00-15:00, 17:00-23:00', 45.0, 4.3, true, true, false, 100, true, -15.615833, -56.070278),
('Siriri Restaurante', 'Culinária regional com música ao vivo', 'Rua Presidente Marques, Lixeira', '(65) 3624-8000', '11:00-15:00, 18:00-00:00', 50.0, 4.4, true, true, true, 120, true, -15.605833, -56.105556),
('Pantaneiro Grill', 'Comida caseira pantaneira', 'Av. Getúlio Vargas, Centro Norte', '(65) 99999-9001', '11:00-15:00, 18:00-22:00', 40.0, 4.2, true, true, false, 90, true, -15.578611, -56.079167),
('Tradição Cuiabana', 'Buffet com pratos tradicionais', 'Rua 24 de Outubro, Bandeirantes', '(65) 99999-9002', '11:00-15:00, 18:00-22:00', 35.0, 4.1, true, true, false, 80, true, -15.625278, -56.092778);

-- =================== JAPONESA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Sushi Zen', 'Restaurante japonês com sushi fresco', 'Shopping Pantanal, Duque de Caxias', '(65) 99999-1101', '12:00-15:00, 18:00-23:00', 85.0, 4.6, true, true, false, 90, true, -15.555833, -56.086389),
('Yamato Cuiabá', 'Culinária japonesa tradicional', 'Av. Miguel Sutil, Jardim Leblon', '(65) 99999-1102', '18:00-23:00', 95.0, 4.5, true, true, true, 100, true, -15.555278, -56.086111),
('Sakura Sushi Bar', 'Sushi bar com ambiente moderno', 'Av. Carmindo de Campos, Santana', '(65) 99999-1103', '18:00-00:00', 110.0, 4.4, false, true, true, 120, true, -15.565556, -56.058333),
('Nihon House', 'Casa japonesa com jardim zen', 'Rua Marechal Deodoro, Centro', '(65) 99999-1104', '12:00-15:00, 19:00-23:00', 120.0, 4.7, true, true, false, 110, true, -15.596944, -56.098056),
('Tokyo Grill', 'Teppanyaki e pratos grelhados', 'Shopping Três Américas, Alvorada', '(65) 99999-1105', '12:00-22:00', 80.0, 4.3, true, true, false, 90, true, -15.575278, -56.062778);

-- =================== FAST FOOD ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('McDonald''s Pantanal', 'Rede internacional de fast food', 'Shopping Pantanal, Duque de Caxias', '(65) 3025-8000', '10:00-22:00', 25.0, 4.2, true, true, false, 45, true, -15.555833, -56.086389),
('Burger King Arena', 'Hambúrgueres grelhados', 'Arena Pantanal, Verdão', '(65) 99999-1201', '11:00-23:00', 30.0, 4.1, true, true, false, 40, true, -15.603333, -56.122778),
('Subway Cuiabá', 'Sanduíches naturais personalizados', 'Av. Getúlio Vargas, Centro Norte', '(65) 99999-1202', '10:00-22:00', 20.0, 4.0, true, true, false, 30, true, -15.578333, -56.078889),
('KFC Shopping', 'Frango frito tradicional', 'Shopping Três Américas, Alvorada', '(65) 99999-1203', '10:00-22:00', 28.0, 4.2, true, false, false, 35, true, -15.575278, -56.062778),
('Bob''s Cuiabá', 'Hambúrgueres e milkshakes brasileiros', 'Av. Miguel Sutil, Duque de Caxias', '(65) 99999-1204', '10:00-23:00', 22.0, 3.9, true, true, false, 40, true, -15.555556, -56.087222);

-- =================== VEGANA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Green Garden', 'Restaurante 100% vegano com pratos orgânicos', 'Rua Isaac Povoas, Centro', '(65) 99999-1301', '11:00-15:00, 18:00-22:00', 45.0, 4.5, true, true, false, 75, true, -15.595000, -56.096389),
('Vida Natural', 'Buffet vegetariano e vegano', 'Av. Fernando Corrêa da Costa, Coxipó', '(65) 99999-1302', '11:00-15:00, 17:00-21:00', 35.0, 4.4, true, true, false, 60, true, -15.615556, -56.069444),
('Veggie House', 'Casa vegana com produtos locais', 'Rua Marechal Deodoro, Centro', '(65) 99999-1303', '08:00-18:00', 40.0, 4.3, true, true, false, 70, true, -15.596667, -56.097778),
('Organic Café', 'Café com opções veganas e orgânicas', 'Shopping Pantanal, Duque de Caxias', '(65) 99999-1304', '09:00-22:00', 30.0, 4.2, true, true, false, 50, true, -15.555833, -56.086389),
('Terra Verde', 'Restaurante naturista com horta própria', 'Av. Miguel Sutil, Jardim Leblon', '(65) 99999-1305', '11:30-15:00, 18:30-22:00', 50.0, 4.6, true, true, false, 85, true, -15.555278, -56.086111);

-- =================== ITALIANA ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Nonna Mia', 'Pizzaria italiana com receitas tradicionais', 'Av. Carmindo de Campos, Santana', '(65) 99999-1401', '18:00-00:00', 60.0, 4.5, true, true, true, 90, true, -15.565278, -56.058056),
('Trattoria Bella Vista', 'Massas artesanais e vinhos italianos', 'Rua Joaquim Murtinho, Centro', '(65) 99999-1402', '18:00-23:00', 80.0, 4.6, true, true, true, 100, true, -15.597500, -56.098611),
('La Pasta', 'Especializada em massas frescas', 'Shopping Três Américas, Alvorada', '(65) 99999-1403', '12:00-22:00', 55.0, 4.3, true, true, false, 75, true, -15.575278, -56.062778),
('Roma Antica', 'Ambiente italiano autêntico', 'Av. Historiador Rubens de Mendonça, Bosque', '(65) 99999-1404', '19:00-00:00', 95.0, 4.7, true, true, true, 120, true, -15.570278, -56.065556),
('Pizzaria Capri', 'Pizza napolitana no forno a lenha', 'Av. Miguel Sutil, Duque de Caxias', '(65) 99999-1405', '18:00-23:00', 50.0, 4.4, true, true, false, 80, true, -15.555556, -56.087222);

-- =================== FITNESS ===================
INSERT INTO locais (nome, descricao, endereco, telefone, horario_funcionamento, preco_medio, avaliacao_media, adequado_criancas, opcoes_vegetarianas, ambiente_noturno, tempo_medio_visita, ativo, latitude, longitude) VALUES
('Academia Corpo & Mente', 'Academia completa com musculação e aeróbicos', 'Av. Fernando Corrêa da Costa, Coxipó', '(65) 99999-1501', '05:00-23:00', 80.0, 4.4, false, true, false, 90, true, -15.615278, -56.069722),
('Smart Fit Cuiabá', 'Rede de academias 24 horas', 'Shopping Pantanal, Duque de Caxias', '(65) 99999-1502', '24h', 60.0, 4.2, false, true, false, 75, true, -15.555833, -56.086389),
('CrossFit Pantanal', 'Box de CrossFit com treinamento funcional', 'Av. Miguel Sutil, Jardim Leblon', '(65) 99999-1503', '06:00-22:00', 120.0, 4.5, false, false, false, 60, true, -15.555278, -56.086111),
('Runners Club', 'Grupo de corrida e assessoria esportiva', 'Parque Tia Nair, Quilombo', '(65) 99999-1504', '06:00-08:00, 17:00-19:00', 50.0, 4.3, false, false, false, 60, true, -15.580556, -56.094167),
('Studio Pilates & Yoga', 'Estúdio especializado em Pilates e Yoga', 'Rua Marechal Deodoro, Centro', '(65) 99999-1505', '06:00-20:00', 100.0, 4.6, false, true, false, 75, true, -15.596667, -56.097778);

-- Inserir as tags para cada local
-- CULTURA
INSERT INTO local_tags (local_id, tag) VALUES 
(1, 'cultura'), (1, 'arte'), (1, 'historia'),
(2, 'cultura'), (2, 'museu'), (2, 'historia'),
(3, 'cultura'), (3, 'historia'),
(4, 'cultura'), (4, 'arte'), (4, 'noturno'),
(5, 'cultura'), (5, 'museu'), (5, 'historia');

-- NATUREZA  
INSERT INTO local_tags (local_id, tag) VALUES
(6, 'natureza'), (6, 'esporte'), (6, 'familia'),
(7, 'natureza'), (7, 'familia'),
(8, 'natureza'), (8, 'familia'), (8, 'agua'),
(9, 'natureza'), (9, 'familia'),
(10, 'natureza'), (10, 'familia'), (10, 'agua');

-- ESPORTE
INSERT INTO local_tags (local_id, tag) VALUES
(11, 'esporte'), (11, 'familia'),
(12, 'esporte'), (12, 'fitness'),
(13, 'esporte'), (13, 'fitness'),
(14, 'esporte'),
(15, 'esporte'), (15, 'agua'), (15, 'fitness');

-- NOTURNO
INSERT INTO local_tags (local_id, tag) VALUES
(16, 'noturno'), (16, 'bar'),
(17, 'noturno'), (17, 'bar'),
(18, 'noturno'), (18, 'bar'),
(19, 'noturno'), (19, 'bar'),
(20, 'noturno');

-- HISTÓRIA
INSERT INTO local_tags (local_id, tag) VALUES
(21, 'historia'), (21, 'cultura'), (21, 'museu'),
(22, 'historia'), (22, 'cultura'), (22, 'museu'),
(23, 'historia'), (23, 'cultura'),
(24, 'historia'), (24, 'museu'), (24, 'agua'),
(25, 'historia'), (25, 'cultura');

-- BAR
INSERT INTO local_tags (local_id, tag) VALUES
(26, 'bar'), (26, 'noturno'),
(27, 'bar'), (27, 'noturno'), (27, 'brasileira'),
(28, 'bar'), (28, 'noturno'),
(29, 'bar'), (29, 'noturno'),
(30, 'bar'), (30, 'noturno');

-- MUSEU
INSERT INTO local_tags (local_id, tag) VALUES
(31, 'museu'), (31, 'cultura'), (31, 'historia'),
(32, 'museu'), (32, 'cultura'),
(33, 'museu'), (33, 'cultura'), (33, 'arte'),
(34, 'museu'), (34, 'cultura'), (34, 'historia'),
(35, 'museu'), (35, 'cultura'), (35, 'historia');

-- ANIMAL
INSERT INTO local_tags (local_id, tag) VALUES
(36, 'animal'), (36, 'familia'), (36, 'natureza'),
(37, 'animal'), (37, 'familia'), (37, 'agua'),
(38, 'animal'), (38, 'natureza'),
(39, 'animal'), (39, 'familia'), (39, 'natureza'),
(40, 'animal'), (40, 'natureza');

-- ARTE
INSERT INTO local_tags (local_id, tag) VALUES
(41, 'arte'), (41, 'cultura'),
(42, 'arte'), (42, 'cultura'),
(43, 'arte'), (43, 'cultura'),
(44, 'arte'), (44, 'cultura'),
(45, 'arte'), (45, 'cultura'), (45, 'historia');

-- SHOPPING
INSERT INTO local_tags (local_id, tag) VALUES
(46, 'shopping'), (46, 'familia'),
(47, 'shopping'), (47, 'familia'),
(48, 'shopping'), (48, 'familia'),
(49, 'shopping'), (49, 'familia'),
(50, 'shopping'), (50, 'familia'), (50, 'historia');

-- FAMÍLIA
INSERT INTO local_tags (local_id, tag) VALUES
(51, 'familia'), (51, 'natureza'),
(52, 'familia'), (52, 'cultura'),
(53, 'familia'), (53, 'agua'), (53, 'parque de diversoes'),
(54, 'familia'), (54, 'animal'), (54, 'natureza'),
(55, 'familia'), (55, 'parque de diversoes');

-- PARQUE DE DIVERSÕES
INSERT INTO local_tags (local_id, tag) VALUES
(56, 'parque de diversoes'), (56, 'familia'),
(57, 'parque de diversoes'), (57, 'familia'),
(58, 'parque de diversoes'), (58, 'familia'),
(59, 'parque de diversoes'), (59, 'familia'), (59, 'natureza'),
(60, 'parque de diversoes'), (60, 'familia');

-- ÁGUA
INSERT INTO local_tags (local_id, tag) VALUES
(61, 'agua'), (61, 'familia'),
(62, 'agua'), (62, 'esporte'), (62, 'familia'),
(63, 'agua'), (63, 'familia'), (63, 'parque de diversoes'),
(64, 'agua'), (64, 'natureza'), (64, 'familia'),
(65, 'agua'), (65, 'esporte'), (65, 'fitness');

-- CHURRASCO
INSERT INTO local_tags (local_id, tag) VALUES
(66, 'churrasco'), (66, 'brasileira'), (66, 'familia'),
(67, 'churrasco'), (67, 'brasileira'), (67, 'familia'),
(68, 'churrasco'), (68, 'brasileira'), (68, 'familia'),
(69, 'churrasco'), (69, 'brasileira'), (69, 'familia'),
(70, 'churrasco'), (70, 'brasileira'), (70, 'familia'), (70, 'cultura');

-- BRASILEIRA
INSERT INTO local_tags (local_id, tag) VALUES
(71, 'brasileira'), (71, 'familia'),
(72, 'brasileira'), (72, 'familia'),
(73, 'brasileira'), (73, 'familia'), (73, 'cultura'),
(74, 'brasileira'), (74, 'familia'),
(75, 'brasileira'), (75, 'familia');

-- JAPONESA
INSERT INTO local_tags (local_id, tag) VALUES
(76, 'japonesa'), (76, 'familia'),
(77, 'japonesa'),
(78, 'japonesa'), (78, 'bar'), (78, 'noturno'),
(79, 'japonesa'), (79, 'cultura'),
(80, 'japonesa'), (80, 'familia');

-- FAST FOOD
INSERT INTO local_tags (local_id, tag) VALUES
(81, 'fast food'), (81, 'familia'),
(82, 'fast food'), (82, 'familia'),
(83, 'fast food'), (83, 'familia'),
(84, 'fast food'), (84, 'familia'),
(85, 'fast food'), (85, 'familia');

-- VEGANA
INSERT INTO local_tags (local_id, tag) VALUES
(86, 'vegana'), (86, 'familia'),
(87, 'vegana'), (87, 'familia'),
(88, 'vegana'), (88, 'familia'),
(89, 'vegana'), (89, 'familia'),
(90, 'vegana'), (90, 'familia'), (90, 'natureza');

-- ITALIANA
INSERT INTO local_tags (local_id, tag) VALUES
(91, 'italiana'), (91, 'familia'),
(92, 'italiana'), (92, 'familia'), (92, 'bar'),
(93, 'italiana'), (93, 'familia'),
(94, 'italiana'), (94, 'bar'), (94, 'noturno'),
(95, 'italiana'), (95, 'familia');

-- FITNESS
INSERT INTO local_tags (local_id, tag) VALUES
(96, 'fitness'), (96, 'esporte'),
(97, 'fitness'), (97, 'esporte'),
(98, 'fitness'), (98, 'esporte'),
(99, 'fitness'), (99, 'esporte'), (99, 'natureza'),
(100, 'fitness'), (100, 'esporte');

