package pucmm.eict.library.ClientService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pucmm.eict.library.ClientService.dto.*;
import pucmm.eict.library.ClientService.service.CatalogueService;
import pucmm.eict.library.ClientService.service.OrderService;
import pucmm.eict.library.ClientService.service.UserService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final CatalogueService catalogueService;

    @Autowired
    public AdminController(UserService userService, OrderService orderService, CatalogueService catalogueService) {
        this.userService = userService;
        this.orderService = orderService;
        this.catalogueService = catalogueService;
    }

    @GetMapping("")
    public String getDashboard(Model model, HttpServletRequest request) {
        List<Order> orders = orderService.getOrders();

        // Procesar los datos para el gráfico de área
        Map<String, Long> salesData = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCreateDate().toLocalDate().toString(), Collectors.counting()));

        List<String> areaLabels = new ArrayList<>(salesData.keySet());
        List<Long> areaData = new ArrayList<>(salesData.values());

        // Procesar los datos para el gráfico de barras
        Map<String, Long> genreData = new HashMap<>();

        for (Order order : orders) {
            UserDTO user = userService.getUserById(order.getUserId());
            order.setUser(user);
            for (CartItemDTO cartItem : order.getCartItems()) {
                Book book = catalogueService.getBookById(cartItem.getBookId(), request);
                if (book != null) {
                    for (String genre : book.getGenres()) {
                        genreData.put(genre, genreData.getOrDefault(genre, 0L) + 1);
                    }
                }
            }
        }

        List<String> barLabels = genreData.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Long> barData = barLabels.stream()
                .map(genreData::get)
                .collect(Collectors.toList());


        model.addAttribute("orders", orders);
        model.addAttribute("areaLabels", areaLabels);
        model.addAttribute("areaData", areaData);
        model.addAttribute("barLabels", barLabels);
        model.addAttribute("barData", barData);

        // Contadores:
        List<Book> allBooks = catalogueService.getCatalogueItems();

        // Obteniendo géneros únicos disponibles
        Set<String> genresAvailable = allBooks.stream()
                .flatMap(book -> {
                    if (book.getGenres() != null) {
                        return book.getGenres().stream();
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toSet());

        model.addAttribute("ordersQuantity", orders.size());
        model.addAttribute("genresQuantity", genresAvailable.size());
        model.addAttribute("booksQuantity", allBooks.size());
        model.addAttribute("userQuantity", userService.getUsers().size());

        return "admin/dashboard/dashboard";
    }


    @GetMapping("/users")
    public String getUsers(Model model) {

        List<UserDTO> users = userService.getUsers();
        model.addAttribute("users", users);

        return "admin/user/list";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("actualUser", new UserDTO());
        model.addAttribute("isEditing", false);
        model.addAttribute("roles", Role.values());
        return "admin/user/create";
    }

    @PostMapping("/users/create")
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok("Usuario creado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al crear el usuario");
        }
    }

    // removeUser
    @PostMapping("/users/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> removeUser(@PathVariable("id") String id) {
        System.out.println("Removing user with id: " + id);

        try {
            userService.removeUser(id);
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar el usuario");
        }
    }

    @PostMapping("/users/change-role/{id}")
    @ResponseBody
    public ResponseEntity<String> changeUserRole(@PathVariable("id") String id) {
        System.out.println("Changing role of user with id: " + id);

        try {
            UserDTO userChanged = userService.changeUserRole(id);
            return ResponseEntity.ok("Rol del usuario cambiado con éxito a " + userChanged.getRole());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al cambiar el rol del usuario");
        }
    }


    @GetMapping("/purchases")
    public String getAdminPurchases(Model model) {
        List<Order> allOrders = orderService.getOrders();
        List<Order> clientOrders = new ArrayList<>();
        for (Order order : allOrders) {
            System.out.println("ID: orden " + order.getId());
            System.out.println("id usuario: " + order.getUserId());
            UserDTO user = userService.getUserById(order.getUserId());
            System.out.println("nombre de usuario: " + user.getUsername());
            if (user != null && user.getRole() == Role.USER) {
                order.setUser(user);
                clientOrders.add(order);
            }
        }

        model.addAttribute("orders", clientOrders);
        return "admin/purchases/list";
    }

}
