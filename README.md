# 💰 ỨNG DỤNG TÀI CHÍNH NHỎ (Mini Financial Management App)
<img width="221" height="206" alt="image" src="https://github.com/user-attachments/assets/18d2a3b5-54ae-4070-a29b-c447bd0055b5" />


>**"Tài Chính Nhỏ"** là ứng dụng di động được xây dựng trên nền tảng **Android** và ngôn ngữ **Kotlin**, giúp người dùng dễ dàng **ghi chép, theo dõi, thống kê** các khoản thu nhập và chi tiêu cá nhân một cách hợp lý. Mục tiêu là cung cấp một giải pháp hiện đại, tự động hóa việc tổng hợp và phân tích dữ liệu tài chính, từ đó hỗ trợ người dùng kiểm soát ngân sách hiệu quả, tránh chi tiêu quá độ.

---

## ✨ TÍNH NĂNG CHÍNH (Key Features)

Ứng dụng cung cấp các chức năng cốt lõi sau để quản lý tài chính cá nhân:

* **Xác thực bảo mật (Auth):** Đăng ký/Đăng nhập bằng Email/Password hoặc Google Sign-In.
***Quản lý giao dịch:** Thêm, xem, xóa các khoản **Thu nhập** và **Chi tiêu**.
* **Thống kê trực quan:** Cung cấp biểu đồ đường (Line Chart) so sánh biến động thu chi và biểu đồ tròn (Pie Chart) tùy chỉnh để phân tích danh mục chi tiêu theo tháng/năm.
* **Cá nhân hóa:** Hỗ trợ **Đa ngôn ngữ** (Tiếng Việt/Tiếng Anh) và **Chế độ Sáng/Tối** (Dark Mode).
* **Hồ sơ:** Cho phép cập nhật thông tin cá nhân và ảnh đại diện.

---

## 🛠️ CÔNG NGHỆ VÀ KIẾN TRÚC (Tech Stack & Architecture)

Dự án được phát triển với các công nghệ hiện đại nhất trong hệ sinh thái Android, tuân thủ kiến trúc MVVM.

### 💻 Frontend & Nền tảng

* **Ngôn ngữ lập trình:** **Kotlin** (ngôn ngữ chính thức cho Android, với các tính năng như Null Safety và Coroutines).
* [cite**UI Toolkit:** **Jetpack Compose** (Declarative UI) - giúp xây dựng giao diện nhanh chóng, mượt mà và dễ quản lý trạng thái.
***Kiến trúc:** **MVVM (Model-View-ViewModel)** kết hợp **Repository Pattern** để tách biệt logic nghiệp vụ và giao diện[cite: 290, 523].
    * Sử dụng **Single Activity Architecture** và **Navigation Compose** để quản lý luồng điều hướng.
  **Bất đồng bộ:** **Kotlin Coroutines** và **Flow/StateFlow** để xử lý các tác vụ I/O (Firebase, nén ảnh) và đồng bộ dữ liệu thời gian thực lên UI (Reactive Programming).

### ☁️ Backend & Cơ sở dữ liệu (Serverless)

* **Backend Platform:** **Google Firebase** (mô hình Serverless).
* **Firebase Authentication:** Quản lý đăng nhập Email/Password, Google Sign-In, và xác thực email.
* **Cloud Firestore:** Cơ sở dữ liệu NoSQL thời gian thực, lưu trữ dữ liệu người dùng (`users` Collection) và giao dịch (`transactions` Sub-collection).
* **Quản lý cấu hình:** **Jetpack DataStore** (thay thế SharedPreferences) để lưu trữ tùy chọn giao diện (Dark Mode).
* **Tối ưu ảnh (Base64):** Ảnh đại diện được nén (max 1024x1024, chất lượng 70%) và mã hóa thành chuỗi **Base64** để lưu trực tiếp vào Firestore, giúp tối ưu chi phí và tốc độ tải so với Firebase Storage.

---

## 🏗 CẤU TRÚC DỮ LIỆU CƠ SỞ (Firestore Schema)

Dữ liệu được tổ chức theo cấu trúc phân cấp để đảm bảo tính riêng tư cho từng người dùng.

### 1. `users` (Collection) 

| Trường | Kiểu dữ liệu | Mô tả chi tiết |
| :--- | :--- | :--- |
| **uid** | String | Mã định danh duy nhất (Primary Key). |
| **email** | String | Email đăng nhập. |
| **avatarUrl** | String? | Chuỗi mã hóa Base64 của ảnh đại diện. |
| **provider** | String | Phương thức đăng nhập (`google`, `password`). |

### 2. `transactions` (Sub-collection)

*Tồn tại trong mỗi document `users/{uid}/transactions`.*

| Trường | Kiểu dữ liệu | Mô tả chi tiết |
| :--- | :--- | :--- |
| **id** | String | Mã giao dịch tự sinh của Firestore. |
| **soTien** | Double |Giá trị giao dịch. |
| **loai** | String | Phân loại chính: `thu` hoặc `chi`. |
| **nhom** | String | Danh mục chi tiết (Ví dụ: `Ăn uống`, `Lương`). |
| **ngayTao** | Date | Thời gian thực tế tạo bản ghi, dùng để sắp xếp lịch sử. |

---

## 🚀 ĐỊNH HƯỚNG PHÁT TRIỂN TƯƠNG LAI (Future Roadmap)

Để nâng cao chất lượng và tính năng ứng dụng, nhóm sẽ tập trung vào các hướng phát triển sau:

* **Tích hợp AI:** Sử dụng **AI** để gợi ý chi tiêu tối ưu và **tự động phân loại** chi tiêu dựa trên nội dung giao dịch.
* **Liên kết tài khoản:** Tích hợp với **tài khoản ngân hàng** và **ví điện tử** (Momo, ZaloPay) để tự động ghi nhận giao dịch.
* **OCR:** Tích hợp tính năng **OCR (nhận dạng ký tự quang học)** để đọc hóa đơn.
* **Hỗ trợ đa nền tảng:** Phát triển phiên bản cho **iOS**.
* **Chia sẻ/Đồng bộ:** Thêm chức năng **chia sẻ** số thu chi với bạn bè/gia đình và **đồng bộ** dữ liệu giữa nhiều thiết bị.

---

## 👥 THÀNH VIÊN NHÓM & PHÂN CÔNG (Group Members & Roles)

| MSSV | Tên | Nhiệm vụ |
| :--- | :--- | :--- |
| 094205000472 | Trần Ngọc Đông | Viết LaTeX, Docx, pptx, Front-end: code giao diện. |
| 089205019819 | Trần Thanh Hiền | Làm Docx, Front-end: code giao diện, Back-end, Chức năng đăng ký/đăng nhập. |
| 087205007254 | Cao Quốc Duy | Back-end, Front-end: code giao diện, Làm Docx. |
| **Giảng viên hướng dẫn** | ThS.Trương Quan Tuấn |. |
