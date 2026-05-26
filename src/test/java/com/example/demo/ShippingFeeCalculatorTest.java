package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class ShippingFeeCalculatorTest {
    @InjectMocks
    private ShippingFeeCalculator shippingFeeCalculator;

    //    Cân nặng nhỏ hơn hoặc bằng 1kg, khoảng cách nhỏ hơn 10km.
    @Test
    @DisplayName("Test case 1")
    void testCase1() {
        double result = shippingFeeCalculator.calculateFee(1, 5);
        assertThat(result).isEqualTo(50000);
    }

    //    Cân nặng lớn hơn 1kg (số nguyên), khoảng cách trong khoảng 10km đến 50km.
    @Test
    @DisplayName("Test case 2")
    void testCase2() {
        double result = shippingFeeCalculator.calculateFee(3, 20);
        assertThat(result).isEqualTo(170000);
    }

    //    Cân nặng là số lẻ (ví dụ: 1.5kg, 2.3kg), khoảng cách lớn hơn 50km.
    @Test
    @DisplayName("Test case 3")
    public void testCase3() {
        double result = shippingFeeCalculator.calculateFee(1.5, 60);
        assertThat(result).isEqualTo(300000);
    }

    //    Khoảng cách đúng 10km
    @Test
    @DisplayName("Test case 4")
    void testCase4() {
        double result = shippingFeeCalculator.calculateFee(1, 10);
        assertThat(result).isEqualTo(100000);
    }

    //    Khoảng cách đúng 50km
    @Test
    void whenDistanceExactly50Km() {
        double result = shippingFeeCalculator.calculateFee(1, 50);
        assertThat(result).isEqualTo(250000);
    }

    //    Kiểm tra trường hợp đầu vào không hợp lệ (cân nặng hoặc khoảng cách <= 0, ném IllegalArgumentException).
    @Test
    @DisplayName("Test case 5")
    void testCase5() {
        assertThatThrownBy(() -> shippingFeeCalculator.calculateFee(0, 10)).isInstanceOf(IllegalArgumentException.class);
    }
}
