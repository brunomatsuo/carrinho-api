# MS-Carrinho

Documentação: https://documenter.getpostman.com/view/7520874/2sA2xh2Xvv

Guia:

- Para realizar qualquer operação, é necessário passar o header Authorization com o bearer token gerado através do Login no microsserviço de Usuários;
- Cada novo usuário deve criar um carrinho, que será único e manterá os itens salvos até que seja realizado o pedido ou até que os itens sejam removidos manualmente;
- A partir do token, o sistema irá reconhecer o usuário e adicionar/remover itens no seu respectivo carrinho;
- Uma vez realizado o pedido, em caso de sucesso, os itens serão removidos do estoque e o carrinho do usuário voltará ao seu estado inicial, sem nenhum item;
- Quando é realizado o pedido, o status de pagamento é definido como 'ENVIADO'. O microsserviço de pagamentos irá receber o pagamento e providenciará a alteração do status, conforme o sucesso/falha do pagamento.