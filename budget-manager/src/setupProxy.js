const proxy = require('http-proxy-middleware');

module.exports = function (app) {
  const endpoint = process.env.REACT_APP_API_ENDPOINT;
  app.use(proxy('/oauth/', {target: endpoint, changeOrigin: true}));
  app.use(proxy('/bm/', {target: endpoint, changeOrigin: true}));
};
