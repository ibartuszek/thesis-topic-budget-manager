const proxy = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(proxy('/oauth/', {target: 'http://localhost:9999', changeOrigin: true}));
  app.use(proxy('/bm/', {target: 'http://localhost:9999', changeOrigin: true}));
};

