# 💰 ỨNG DỤNG TÀI CHÍNH NHỎ (Mini Financial Management App)

![QR Code Link Dự Án](https://i.imgur.com/KxJ6wQ2.png) 
*Quét mã QR để truy cập link GitHub này.*

> [cite_start]**"Tài Chính Nhỏ"** là ứng dụng di động được xây dựng trên nền tảng **Android** và ngôn ngữ **Kotlin**, giúp người dùng dễ dàng **ghi chép, theo dõi, thống kê** các khoản thu nhập và chi tiêu cá nhân một cách hợp lý[cite: 27, 47]. [cite_start]Mục tiêu là cung cấp một giải pháp hiện đại, tự động hóa việc tổng hợp và phân tích dữ liệu tài chính, từ đó hỗ trợ người dùng kiểm soát ngân sách hiệu quả, tránh chi tiêu quá độ[cite: 83, 47].

---

## ✨ TÍNH NĂNG CHÍNH (Key Features)

[cite_start]Ứng dụng cung cấp các chức năng cốt lõi sau để quản lý tài chính cá nhân[cite: 29]:

* [cite_start]**Xác thực bảo mật (Auth):** Đăng ký/Đăng nhập bằng Email/Password hoặc Google Sign-In[cite: 30, 31, 37, 440].
* [cite_start]**Quản lý giao dịch:** Thêm, xem, xóa các khoản **Thu nhập** và **Chi tiêu**[cite: 32, 33, 351].
* [cite_start]**Thống kê trực quan:** Cung cấp biểu đồ đường (Line Chart) so sánh biến động thu chi và biểu đồ tròn (Pie Chart) tùy chỉnh để phân tích danh mục chi tiêu theo tháng/năm[cite: 34, 488].
* [cite_start]**Cá nhân hóa:** Hỗ trợ **Đa ngôn ngữ** (Tiếng Việt/Tiếng Anh) và **Chế độ Sáng/Tối** (Dark Mode)[cite: 491, 492].
* [cite_start]**Hồ sơ:** Cho phép cập nhật thông tin cá nhân và ảnh đại diện[cite: 355].

---

## 🛠️ CÔNG NGHỆ VÀ KIẾN TRÚC (Tech Stack & Architecture)

[cite_start]Dự án được phát triển với các công nghệ hiện đại nhất trong hệ sinh thái Android, tuân thủ kiến trúc MVVM[cite: 523].

### 💻 Frontend & Nền tảng

* [cite_start]**Ngôn ngữ lập trình:** **Kotlin** (ngôn ngữ chính thức cho Android, với các tính năng như Null Safety và Coroutines)[cite: 107, 506].
* [cite_start]**UI Toolkit:** **Jetpack Compose** (Declarative UI) - giúp xây dựng giao diện nhanh chóng, mượt mà và dễ quản lý trạng thái[cite: 508, 509].
* [cite_start]**Kiến trúc:** **MVVM (Model-View-ViewModel)** kết hợp **Repository Pattern** để tách biệt logic nghiệp vụ và giao diện[cite: 290, 523].
    * [cite_start]Sử dụng **Single Activity Architecture** và **Navigation Compose** để quản lý luồng điều hướng[cite: 427, 428].
* [cite_start]**Bất đồng bộ:** **Kotlin Coroutines** và **Flow/StateFlow** để xử lý các tác vụ I/O (Firebase, nén ảnh) và đồng bộ dữ liệu thời gian thực lên UI (Reactive Programming)[cite: 380, 381, 496].

### ☁️ Backend & Cơ sở dữ liệu (Serverless)

* [cite_start]**Backend Platform:** **Google Firebase** (mô hình Serverless)[cite: 519].
    * [cite_start]**Firebase Authentication:** Quản lý đăng nhập Email/Password, Google Sign-In, và xác thực email[cite: 399, 439].
    * [cite_start]**Cloud Firestore:** Cơ sở dữ liệu NoSQL thời gian thực, lưu trữ dữ liệu người dùng (`users` Collection) và giao dịch (`transactions` Sub-collection)[cite: 400, 443].
* [cite_start]**Quản lý cấu hình:** **Jetpack DataStore** (thay thế SharedPreferences) để lưu trữ tùy chọn giao diện (Dark Mode)[cite: 407, 470].
* [cite_start]**Tối ưu ảnh (Base64):** Ảnh đại diện được nén (max 1024x1024, chất lượng 70%) và mã hóa thành chuỗi **Base64** để lưu trực tiếp vào Firestore, giúp tối ưu chi phí và tốc độ tải so với Firebase Storage[cite: 468, 494].

---

## 🏗 CẤU TRÚC DỮ LIỆU CƠ SỞ (Firestore Schema)

[cite_start]Dữ liệu được tổ chức theo cấu trúc phân cấp để đảm bảo tính riêng tư cho từng người dùng[cite: 444, 445].

### [cite_start]1. `users` (Collection) [cite: 446]

| Trường | Kiểu dữ liệu | Mô tả chi tiết |
| :--- | :--- | :--- |
| **uid** | String | Mã định danh duy nhất (Primary Key). |
| **email** | String | Email đăng nhập. |
| **avatarUrl** | String? | [cite_start]Chuỗi mã hóa Base64 của ảnh đại diện[cite: 450, 597]. |
| **provider** | String | [cite_start]Phương thức đăng nhập (`google`, `password`)[cite: 451, 597]. |

### [cite_start]2. `transactions` (Sub-collection) [cite: 454]

*Tồn tại trong mỗi document `users/{uid}/transactions`.*

| Trường | Kiểu dữ liệu | Mô tả chi tiết |
| :--- | :--- | :--- |
| **id** | String | Mã giao dịch tự sinh của Firestore. |
| **soTien** | Double | [cite_start]Giá trị giao dịch[cite: 459, 617]. |
| **loai** | String | [cite_start]Phân loại chính: `thu` hoặc `chi`[cite: 460, 617]. |
| **nhom** | String | [cite_start]Danh mục chi tiết (Ví dụ: `Ăn uống`, `Lương`)[cite: 461, 617]. |
| **ngayTao** | Date | [cite_start]Thời gian thực tế tạo bản ghi, dùng để sắp xếp lịch sử[cite: 463, 617]. |

---

## 🚀 ĐỊNH HƯỚNG PHÁT TRIỂN TƯƠNG LAI (Future Roadmap)

[cite_start]Để nâng cao chất lượng và tính năng ứng dụng, nhóm sẽ tập trung vào các hướng phát triển sau[cite: 844]:

* [cite_start]**Tích hợp AI:** Sử dụng **AI** để gợi ý chi tiêu tối ưu và **tự động phân loại** chi tiêu dựa trên nội dung giao dịch[cite: 43, 845, 846].
* [cite_start]**Liên kết tài khoản:** Tích hợp với **tài khoản ngân hàng** và **ví điện tử** (Momo, ZaloPay) để tự động ghi nhận giao dịch[cite: 42, 848].
* [cite_start]**OCR:** Tích hợp tính năng **OCR (nhận dạng ký tự quang học)** để đọc hóa đơn[cite: 847].
* [cite_start]**Hỗ trợ đa nền tảng:** Phát triển phiên bản cho **iOS**[cite: 842].
* [cite_start]**Chia sẻ/Đồng bộ:** Thêm chức năng **chia sẻ** số thu chi với bạn bè/gia đình và **đồng bộ** dữ liệu giữa nhiều thiết bị[cite: 41, 850, 851].

---

## 👥 THÀNH VIÊN NHÓM & PHÂN CÔNG (Group Members & Roles)

| MSSV | Tên | Nhiệm vụ |
| :--- | :--- | :--- |
| 094205000472 | Trần Ngọc Đông | [cite_start]Viết LaTeX, Docx, pptx, Front-end: code giao diện[cite: 23]. |
| 089205019819 | Trần Thanh Hiền | [cite_start]Làm Docx, Front-end: code giao diện, Back-end, Chức năng đăng ký/đăng nhập[cite: 23]. |
| 087205007254 | Cao Quốc Duy | [cite_start]Back-end, Front-end: code giao diện, Làm Docx[cite: 23]. |
| **Giảng viên hướng dẫn** | ThS. [cite_start]Trương Quan Tuấn |[cite: 19]. |
