package com.biomerieux.level1;

import java.util.List;

public record Order(String id, List<Item> items) {
}
