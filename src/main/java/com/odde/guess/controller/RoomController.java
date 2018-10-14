package com.odde.guess.controller;

import com.odde.guess.model.Room;
import com.odde.guess.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private static final String WIN_MESSAGE = "You Win!";
    private static final String WIN_RESULT = "4A0B";
    private final RoomRepository repo;

    @Autowired
    public RoomController(RoomRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ModelAndView rooms(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/rooms/index");
        return view;
    }

    @GetMapping("/create")
    public String createRoom() {
        return "/rooms/create";
    }

    @PostMapping("/create")
    public String submitCreateRoom(String secret) {
        Room room = repo.save(new Room(secret));
        return "redirect:/rooms/show/" + room.getId();
    }


    @GetMapping("/show/{id}")
    public ModelAndView show(@PathVariable("id") String id) {
        return showMessage("");
    }

    @PostMapping("/show/{id}")
    public ModelAndView guess(@PathVariable("id") long id, String guess) {
        String message = repo.findById(id).verify(guess);
        if (message.equals(WIN_RESULT))
            message = WIN_MESSAGE;
        return showMessage(message);
    }

    private ModelAndView showMessage(String message) {
        ModelAndView modelAndView = new ModelAndView("/rooms/show");
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}
