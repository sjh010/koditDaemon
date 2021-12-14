//======================================================
/* Copyright (c) 1999-2015 INZISOFT Co., Ltd. All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited 
 * All information contained herein is, and remains the property of INZISOFT Co., Ltd.
 * The intellectual and technical concepts contained herein are proprietary to INZISOFT Co., Ltd.
 * Dissemination of this information or reproduction of this material is 
 * strictly forbidden unless prior written permission is obtained from INZISOFT Co., Ltd.
 * Proprietary and Confidential.
 */
//======================================================

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inzisoft.crypto;

public class ARIACryptoJNI
{
	/**
	 * 암복호화 객체 생성 
	 * 
	 * @return 암복호화 객체 포인터 : 성공
	 * 		   0 : 실패
	 */
	public static native long CreateObj();
	
	/**
	 * 암복호화 객체 해제
	 * 
	 * @param inziARIA				[in] 암복호화 객체 포인터	
	 */
	public static native void DestroyObj(long inziARIA);
	
	/**
	 * 키 설정
	 * 
	 * @param inziARIA				[in] 암복호화 객체 포인터	
	 * @param key					[in] 키 
	 */
	public static native boolean SetStringKey(long inziARIA, String key);
	
	public static native boolean SetBinaryKey(long inziARIA, byte[] key);
	public static native void SetKeyLength(long inziARIA, int keyLength);
	public static native void SetBlockPaddingMode(long inziARIA, int blockPaddingMode);
	public static native void SetChainingOperMode(long inziARIA, int chainingOperMode);
	
	/**
	 * 메모리에 있는 원본 데이터를 할당된 메모리로 암호화를 수행(memory -> memory)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param oriData					[in] 원본 데이터 
	 * @param encryptData				[out] 암호화 데이터
	 * @param allowMultipleEncryption	[in] 원본 데이터가 암호화되어 있는 경우의 암호화 수행여부
	 * 											true: 암호화 수행, false: 암호화를 수행하지 않음
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Encrypt(long inziARIA, byte[] oriData, byte[] encryptData, boolean allowMultipleEncryption);
	
	/**
	 * 파일에 있는 원본 데이터를 할당된 메모리로 암호화를 수행(file -> memory)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param oriFileName				[in] 원본 데이터 파일 경로
	 * @param encryptData				[out] 암호화 데이터
	 * @param allowMultipleEncryption	[in] 원본 데이터가 암호화되어 있는 경우의 암호화 수행여부
	 * 											true: 암호화 수행, false: 암호화를 수행하지 않음
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Encrypt(long inziARIA, String oriFileName, byte[] encryptData, boolean allowMultipleEncryption);
	
	/**
	 * 메모리에 있는 원본 데이터를 암호화하여 파일로 저장(memory -> file)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param oriData					[in] 원본 데이터
	 * @param encFileName				[in] 암호화 데이터를 저장할 파일 경로
	 * @param allowMultipleEncryption	[in] 원본 데이터가 암호화되어 있는 경우의 암호화 수행여부
	 * 											true: 암호화 수행, false: 암호화를 수행하지 않음
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Encrypt(long inziARIA, byte[] oriData, String encFileName, boolean allowMultipleEncryption);
	
	/**
	 * 파일에 있는 원본 데이터를 암호화하여 파일로 저장(file -> file)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param oriFileName				[in] 원본 데이터 파일 경로
	 * @param encFileName				[in] 암호화 데이터를 저장할 파일 경로
	 * @param allowMultipleEncryption	[in] 원본 데이터가 암호화되어 있는 경우의 암호화 수행여부
	 * 											true: 암호화 수행, false: 암호화를 수행하지 않음
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Encrypt(long inziARIA, String oriFileName, String encFileName, boolean allowMultipleEncryption);
	
	/**
	 * memory -> memory로 암호화 하는 경우 암호화 데이터의 크기 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param oriData					[in] 원본 데이터	 
	 * @return 양수 : 암호화 데이터 크기
	 * 		   0 : 암호화 데이터 크기를 구할 수 없음, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native int GetEncryptDataSize(long inziARIA, byte[] oriData);
	
	/**
	 * file -> memory로 암호화 하는 경우 암호화 데이터의 크기 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param oriFileName				[in] 원본 테이터 파일 경로 
	 * @return 양수 : 암호화 데이터 크기
	 * 		   0 : 암호화 데이터 크기를 구할 수 없음, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native int GetEncryptDataSize(long inziARIA, String oriFileName);
	
	/**
	 * 메모리에 있는 암호화 데이터를 할당된 메모리로 복호화를 수행(memory -> memory)
	 * 
	 * @param inziAES					[in] 암복호화 객체 포인터	
	 * @param encData					[in] 암호화 데이터 
	 * @param decryptData				[out] 복호화 데이터 
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Decrypt(long inziARIA, byte[] encData, byte[] decryptData);
	
	/**
	 * 파일에 있는 암호화 데이터를 할당된 메모리로 복호화를 수행(file -> memory)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encFileName				[in] 암호화 데이터 파일 경로
	 * @param decryptData				[out] 복호화 데이터
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Decrypt(long inziARIA, String encFileName, byte[] decryptData);
	
	/**
	 * 메모리에 있는 암호화 데이터를 복호화하여 파일로 저장(memory -> file)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encData					[in] 암호화 데이터
	 * @param decFileName				[in] 복호화 데이터를 저장할 파일 경로
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Decrypt(long inziARIA, byte[] encData, String decFileName);
	
	/**
	 * 파일에 있는 암호화 데이터를 복호화하여 파일로 저장(file -> file)
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encFileName				[in] 암호화 데이터 파일 경로
	 * @param decFileName				[in] 복호화 데이터를 저장할 파일 경로
	 * @return true : 성공
	 * 		       false : 실패, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean Decrypt(long inziARIA, String encFileName, String decFileName);
	
	/**
	 * memory -> memory로 복호화 하는 경우 복호화 데이터의 크기 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encData					[in] 암호화 데이터	 
	 * @return 양수 : 복호화 데이터 크기
	 * 		   0 : 복호화 데이터 크기를 구할 수 없음, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native int GetDecryptDataSize(long inziARIA, byte[] encData);
	
	/**
	 * file -> memory로 복호화 하는 경우 복호화 데이터의 크기 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encFileName				[in] 암호화 테이터 파일 경로 
	 * @return 양수 : 복호화 데이터 크기
	 * 		   0 : 복호화 데이터 크기를 구할 수 없음, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native int GetDecryptDataSize(long inziARIA, String encFileName);
	
	/**
	 * 데이터의 암복호화 여부 확인 및 암호화 데이터인 경우 암호화 버전 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encData					[in] 암호화 데이터
	 * @param majorVer					[out] 메이저 버전 번호
	 * @param minorVer					[out] 마이너 버전 번호
	 * @return true : 암호화 데이터
	 * 		   false : 암호화 데이터가 아니거나 확인 할 수 없음, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean GetEncVer(long inziARIA, byte[] encData, int[] majorVer, int[] minorVer);
	
	/**
	 * 파일의 암복호화 여부 확인 및 암호화 파일인 경우 암호화 버전 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @param encFileName				[in] 암호화 데이터 파일 경로
	 * @param majorVer					[out] 메이저 버전 번호
	 * @param minorVer					[out] 마이너 버전 번호
	 * @return true : 암호화 데이터
	 * 		   false : 암호화 데이터가 아니거나 확인 할 수 없음, 자세한 에러는 GetErrNo를 통해서 확인
	 */
	public static native boolean GetEncVer(long inziARIA, String encFileName, int[] major, int[] minorVer);
	
	
	/**
	 * 암복호화 실패 시 상세 에러코드 가져오기
	 * 
	 * @param inziARIA					[in] 암복호화 객체 포인터	
	 * @return 	0 함수가 성공적으로 수행되었습니다.
	 *			19002 input data가 NULL입니다.
	 *			19003 메모리 할당을 할 수 없습니다. 메모리의 여유공간이 충분한지 확인해 보시기 바랍니다.
	 *			19004 키가 설정되지 않았습니다.
	 *			19005 결과 데이터를 저장할 메모리가 할당되어 있지 않습니다. 결과 데이터를 저장할 메모리를 할당하셨는지 확인해 보시기 바랍니다.
	 *			19006 파일 이름이 없습니다. 파일 이름을 확인해 보시기 바랍니다.
	 *			19007 파일을 열 수 없습니다. 해당 파일이 사용중인지 또는 디스크 여유공간이 충분한지 확인해 보시기 바랍니다. 파일 경로, 권한 확인
	 *			19008 암호화되지 않은 데이터를 복호화하려고 합니다.
	 *			19009 암호화 데이터 내용이 유효하지 않습니다. 암호화 데이터가 손상된것으로 보입니다.
	 *			19010 암호화된 데이터를 암호화하려고 합니다. 기존 암호화 데이터에 다시 암호화를 하시려면 allowMultipleEncryption값을 1로 넘겨주십시오.
	 *			19011 복호화한 데이터가 원본 데이터와 일치하지 않습니다. 암호화시의 키와 복호화시의 키가 다르거나 암호화 데이터가 손상되었습니다.
	 *			19012 (내부에러) 담당자에게 문의하세요.
	 *			19013 암호화, 복호화 된 파일명 수정에 실패하였습니다.
	 *			19014 파일 쓰기에 실패하였습니다.
	 *			19015 임시파일 생성에 실패하였습니다.
	 *			19999 암복호화 오브젝트가 없습니다. 암복호화 오브젝트를 생성하셨는지 확인해 보시기 바랍니다.
	 */
	public static native int GetErrNo(long inziARIA);

	static
	{
		System.loadLibrary("InziiscFileCrypt");
		System.loadLibrary("InziiscFileCryptJNI");
	}
}
