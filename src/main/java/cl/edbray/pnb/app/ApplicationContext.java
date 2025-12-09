/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.edbray.pnb.app;

import cl.edbray.pnb.controller.LoginController;
import cl.edbray.pnb.controller.ProductController;
import cl.edbray.pnb.controller.SaleController;
import cl.edbray.pnb.controller.UserController;
import cl.edbray.pnb.service.ProductService;
import cl.edbray.pnb.service.SaleService;
import cl.edbray.pnb.service.UserService;
import cl.edbray.pnb.repository.UserRepository;
import cl.edbray.pnb.repository.SaleRepository;
import cl.edbray.pnb.repository.ProductRepository;
import cl.edbray.pnb.repository.impl.ProductRepositoryMysql;
import cl.edbray.pnb.repository.impl.SaleRepositoryMysql;
import cl.edbray.pnb.repository.impl.UserRepositoryMysql;
import cl.edbray.pnb.utils.MysqlDBConnectionManager;

/**
 * Contenedor de Inversión de Control (IoC) manual Responsable de:
 *
 * - Crear todas las instancias de repositorios, servicios y controladores
 * - Inyectar dependencias mediante constructores
 * - Proporcionar acceso centralizado a los componentes.
 *
 * @author eduardo
 */
public class ApplicationContext {

    private static ApplicationContext instance;

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private SaleRepository saleRepository;

    private UserService userService;
    private ProductService productService;
    private SaleService saleService;

    private LoginController loginController;
    private UserController userController;
    private ProductController productController;
    private SaleController saleController;

    private ApplicationContext() {
        init();
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    private void init() {
        System.out.println("🔧 Inicializando ApplicationContext...");

        initRepositories();
        initServices();
        initControllers();

        System.out.println("✅ ApplicationContext inicializado correctamente");
    }

    private void initRepositories() {
        System.out.println("  📦 Creando repositorios Mock...");

        MysqlDBConnectionManager connectionFactory = new MysqlDBConnectionManager();

        userRepository = new UserRepositoryMysql();
        productRepository = new ProductRepositoryMysql();
        saleRepository = new SaleRepositoryMysql(connectionFactory);

        System.out.println("  ✓ Repositorios creados");
    }

    private void initServices() {
        System.out.println("  💼 Creando servicios e inyectando repositorios...");

        userService = new UserService(userRepository);
        productService = new ProductService(productRepository);
        saleService = new SaleService(saleRepository);

        System.out.println("  ✓ Servicios creados");
    }

    private void initControllers() {
        System.out.println("  🎮 Creando controladores e inyectando servicios...");

        loginController = new LoginController(userService);
        userController = new UserController(userService);
        productController = new ProductController(productService);
        saleController = new SaleController(saleService);

        System.out.println("  ✓ Controladores creados");
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public UserController getUserController() {
        return userController;
    }

    public ProductController getProductController() {
        return productController;
    }

    public SaleController getSaleController() {
        return saleController;
    }

    public static void reset() {
        instance = null;
    }
}
