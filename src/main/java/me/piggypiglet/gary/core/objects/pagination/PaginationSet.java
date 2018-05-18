package me.piggypiglet.gary.core.objects.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationSet {
    private List<PaginationPage> pages;

    public PaginationSet() {
        pages = new ArrayList<>();
    }

    public PaginationSet addPages(List<PaginationPage> pages) {
        this.pages = pages;

        return this;
    }

    public PaginationPage getPage(Object emote) {
        AtomicReference<PaginationPage> page = new AtomicReference<>();

         pages.forEach(page_ -> {
            if (page_.getEmote().equals(emote)) {
                page.set(page_);
            }
         });

         return page.get();
    }
}
