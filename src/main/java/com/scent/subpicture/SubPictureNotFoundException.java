package com.scent.subpicture;

//RuntimeException을 상속받아서 사용자 정의 예외 클래스인 TextNotFoundException을 정의합니다.
@SuppressWarnings("serial")
public class SubPictureNotFoundException extends RuntimeException {
// 생성자 메서드입니다. 주어진 id를 사용하여 예외 메시지를 생성하여 부모 생성자에 전달합니다.
public SubPictureNotFoundException(Long id) {
   super("SubPicture not found with id: " + id);
}
}