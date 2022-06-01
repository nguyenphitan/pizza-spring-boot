# Website bán hàng và quản lí cho cửa hàng bán pizza

### Build Web Service Using

- Front-end: 
	* HTML/CSS
	* Javascrip
	* Bootstrap
	* Jquery
- Back-end: 
	* Java Spring boot
	* Spring data jpa
	* Spring security
	* Lombok
	* Mysql database
	* Thymeleaf
	* Spring boot devtools

### Version

* JDK 8
* Spring 2.6.6

### IDE

* Esclip 
* MYSQL Workbench

### 1. Mô tả dự án.
- Hệ thống quản lý bán hàng cho một cửa hàng bán bánh Pizza.
(*) Các chức năng:
- Khách hàng: 
	+ Đăng ký tài khoản, đăng nhập, đăng xuất.
	+ Chỉnh sửa thông tin cá nhân.
	+ Xem menu.
	+ Tìm kiếm món ăn theo tên.
	+ Lọc sản phẩm theo danh mục.
	+ Thêm giỏ hàng, thanh toán giỏ hàng.
	+ Thanh toán qua ví điện từ hoặc thanh toán khi nhận hàng.
	+ Xem lịch sử mua hàng, hủy đơn hàng.
	+ Chat với admin qua Zalo.

- Nhân viên:
	+ Gồm có các chức năng như khách hàng.
	+ Quản lý danh mục sản phẩm.
	+ Quản lý sản phẩm.
	+ Quản lý banner.
	+ Quản lý hóa đơn.
	
- Admin:
	+ Gồm có các chức năng của nhân viên.
	+ Quản lý mã giảm giá.
	+ Quản lý tài khoản.
	+ Quản lý nhân viên.
	+ Chấm công nhân viên.
	+ Tính lương nhân viên.
	+ Thống kê doanh thu theo tháng.

### 2. Nhiệm vụ - đóng góp của các thành viên trong team.
- Nguyễn Phi Tân: 
	+ Quản lý tài khoản.
	+ Quản lý nhân viên.
	+ Quản lý mã giảm giá.
	+ Thống kê doanh thu.
	+ Chấm công nhân viên.
	+ Tính lương nhân viên.
	+ Phân quyền: ADMIN, Nhân viên, Khách hàng.
	+ Sử dụng token định danh người dùng.
	+ Mã hóa mật khẩu bằng sercret key.
	+ Liên kết ví điện tử.
	+ Quản lý code của team, merge code.
	+ Test code.
	+ Fix bug.
	+ Viết báo cáo.

- Đỗ Công Ban:
	+ Dựng base giao diện.
	+ Đăng nhập, đăng ký.
	+ Lọc sản phẩm.
	+ Tìm kiếm sản phẩm.
	+ Quản lý hóa đơn
	+ Quản lý banner.
	+ Quản lý giỏ hàng.
	+ Chỉnh sửa thông tin cá nhân
	+ Xem lịch sử mua hàng, hủy đơn hàng.
	+ Tích hợp chat qua Zalo cho hệ thống.
	+ Viết báo cáo.

- Phan Hoàng Nguyên:
	+ Quản lý danh mục sản phẩm.
	+ Quản lý sản phẩm.


### 3. Thiết kế hệ thống.
![image](https://user-images.githubusercontent.com/62367845/171392898-da2d0a1e-586a-4e46-887e-267e0bdef4a1.png)


