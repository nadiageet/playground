package com.example.playground.gift.api;

import com.example.playground.gift.mapper.GiftMapper;
import com.example.playground.gift.request.UseGiftRequest;
import com.example.playground.gift.response.GiftDTO;
import com.example.playground.gift.service.GiftService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gift")
public class GiftController {


    private final GiftService giftService;

    private final GiftMapper giftMapper;


    public GiftController(GiftService giftService, GiftMapper giftMapper) {
        this.giftService = giftService;
        this.giftMapper = giftMapper;
    }

    @PostMapping("/use")
    public void useGift(@RequestBody @Valid UseGiftRequest request) {
        giftService.useGift(request.giftId());
    }

    @GetMapping
    public List<GiftDTO> getAllUserGifts() {
        return giftService.findAllUserGifts().stream()
                .map(giftMapper::mapToDto)
                .toList();
    }


}
