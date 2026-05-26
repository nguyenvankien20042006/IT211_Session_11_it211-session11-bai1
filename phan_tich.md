# Phân tích logic lỗi tính phí vận chuyển

## 1. Lỗi tính phí theo cân nặng (weightFee)

### Quy tắc nghiệp vụ

- 1kg đầu tiên: 50.000 VND
- Mỗi kg tiếp theo **hoặc phân số của kg**: +10.000 VND

Điều này có nghĩa:

- 1.1kg → tính như 2kg
- 1.5kg → tính như 2kg
- 2.3kg → tính như 3kg

=> Phần cân nặng vượt quá 1kg phải được **làm tròn lên** (`Math.ceil`).

---

### Code hiện tại

```java
weightFee =50000+(Math.

floor(weightKg -1) *10000);
```

### Vấn đề

Code đang dùng `Math.floor()`.

`Math.floor()` sẽ làm tròn xuống nên gây sai logic với số lẻ.

Ví dụ:

#### Trường hợp 1: 1.5kg

```java
Math.floor(1.5-1)
=Math.

floor(0.5)
=0
```

Kết quả:

```java
50000+0=50000
```

Nhưng đúng ra:

- 1.5kg phải tính thêm 1kg tiếp theo
- Phí đúng:

```java
50000+10000=60000
```

---

#### Trường hợp 2: 2.3kg

```java
Math.floor(2.3-1)
=Math.

floor(1.3)
=1
```

Kết quả:

```java
50000+10000=60000
```

Nhưng đúng ra:

- 2.3kg phải làm tròn thành 3kg
- Có 2kg tiếp theo sau kg đầu tiên

Phí đúng:

```java
50000+20000=70000
```

---

### Nguyên nhân lỗi

Hệ thống đang:

- Làm tròn xuống (`floor`)
- Trong khi nghiệp vụ yêu cầu:
    - mọi phần lẻ của kg đều phải tính thành 1kg
    - tức là phải làm tròn lên (`ceil`)

---

## 2. Lỗi tính phí khoảng cách (distanceFee)

### Quy tắc nghiệp vụ

- Dưới 10km → không tính thêm
- Từ 10km đến dưới 50km → 5.000 VND/km
- Từ 50km trở lên → 4.000 VND/km

---

### Code hiện tại

```java
if(distanceKm< 10){
distanceFee =0;
        }else if(distanceKm< 50){
distanceFee =distanceKm *5000;
        }else{
distanceFee =distanceKm *4000;
        }
```

---

## Vấn đề tại ngưỡng biên

### Trường hợp 49km

```java
49*5000=245000
```

Logic này đúng vì:

- 49km thuộc khoảng:
    - từ 10km đến dưới 50km

---

### Trường hợp 50km

```java
50*4000=200000
```

Mặc dù code chạy đúng theo điều kiện hiện tại, nhưng có vấn đề logic nghiệp vụ:

- Chỉ cần tăng từ 49km → 50km
- Phí giảm từ:
    - 245.000 → 200.000

Điều này bất hợp lý vì:

- quãng đường tăng
- nhưng phí lại giảm

---

## Nguyên nhân lỗi

Hệ thống đang:

- áp dụng toàn bộ mức phí mới cho toàn bộ quãng đường

Trong khi logic hợp lý hơn thường là:

- 10km đầu tiên miễn phí
- từ 10 → 49km tính 5.000/km
- phần vượt 50km mới tính 4.000/km

Hiện tại code chưa xử lý theo kiểu tính lũy tiến nên gây ra hiện tượng:

- khoảng cách tăng
- nhưng tổng phí lại giảm tại mốc 50km

---

# Kết luận

## Lỗi chính của hệ thống

### 1. Sai logic làm tròn cân nặng

- Đang dùng:

```java
Math.floor()
```

- Đúng phải dùng:

```java
Math.ceil()
```

để đảm bảo:

- mọi phần lẻ của kg đều được tính thêm phí.

---

### 2. Logic phí khoảng cách chưa hợp lý tại ngưỡng biên

- Khi chuyển từ 49km → 50km:
    - phí bị giảm đột ngột
- Nguyên nhân:
    - áp dụng toàn bộ đơn giá mới cho toàn bộ quãng đường
    - thay vì tính lũy tiến theo từng khoảng.