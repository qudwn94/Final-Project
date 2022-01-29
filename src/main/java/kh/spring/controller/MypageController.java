package kh.spring.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kh.spring.dto.BoardDTO;
import kh.spring.dto.BoardReplyDTO;
import kh.spring.dto.JoinChalDTO;
import kh.spring.dto.MemberDTO;
import kh.spring.service.BoardReplyService;
import kh.spring.service.MypageService;
import kh.spring.dto.CertiDTO;

@Controller
@RequestMapping("/mypage/")
public class MypageController {

	@Autowired
	private HttpSession session;
	@Autowired
	private MypageService memberService;
	@Autowired
	private BoardReplyService brService;
	@Autowired
	private MypageService mService;


	@RequestMapping("updateUserInfo")
	public String  updateUesrInfoPage(Model model, int seq) {
		MemberDTO memberDTO = memberService.selectBySeq(seq);
		model.addAttribute("user",memberDTO);
		return "user/updateUserInfo";
	}

	@RequestMapping("update")
	public String update(HttpServletRequest httpServletRequest,Model model) {
		int seq = Integer.parseInt(httpServletRequest.getParameter("seq"));
		String phone = httpServletRequest.getParameter("phone");
		String email = httpServletRequest.getParameter("email");
		String address1 = httpServletRequest.getParameter("address1");
		String address2 = httpServletRequest.getParameter("address2");
		String pw = httpServletRequest.getParameter("pw");
		MemberDTO memberDTO = memberService.selectBySeq(seq);
		memberDTO.setPhone(phone);
		memberDTO.setEmail(email);
		memberDTO.setAddress1(address1);
		memberDTO.setAddress2(address2);
		memberDTO.setPw(pw);
		model.addAttribute("user",memberDTO);
		memberService.update(memberDTO);
		return "user/mypage";
	}

	@RequestMapping("updateUserProfile")
	public String updateUserProfile(Model model, int seq) {
		MemberDTO memberDTO = memberService.selectBySeq(seq);
		model.addAttribute("user",memberDTO);
		return "user/updateProfile";
	}

	@RequestMapping("updateUserProfileAction")
	public String updateUserProfileAction(Model model,HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws IOException {
		int seq = Integer.parseInt(httpServletRequest.getParameter("seq"));
		String realPath = session.getServletContext().getRealPath("files");
		System.out.print(realPath);
		String filename ="ssh.png";
		//		FileCopyUtils.copy(file.getBytes(), new File(realPath+"/"+filename));
		File f = new File(realPath+"/"+filename);
		MemberDTO memberDTO = memberService.selectBySeq(seq);
		model.addAttribute("user",memberDTO);
		return "user/updateProfile";
	}

	//회원 마이페이지 홈
	@RequestMapping("info")
	public String mypage(Model model ,int seq, HttpServletRequest request) {
		MemberDTO memberDTO = memberService.selectBySeq(seq);
		model.addAttribute("user",memberDTO);
		System.out.print(memberDTO.toString());
		return "user/mypage";

	}
	//참여중인 목록
	@RequestMapping("chaling")
	public String chalingPage(Model model,String nickName ,HttpServletRequest request) {
		//      // 로그인아이디 세션값 저장.
		//      HttpSession session = request.getSession();
		//      session.setAttribute("loginID", "qweobnk");
		//      // 로그인아이디 세션값 꺼내기.
		//      String id = (String) session.getAttribute("loginID");
		//      System.out.println(id);
		//      // 아이디값으로 정보찾기
		//      MemberDTO info = brService.searchInfoById(id);
		//      String joinChalNickname = info.getNickname();
		//      System.out.println(joinChalNickname);
		//      // 닉네임 세션값 저장.
		//      session.setAttribute("joinChalNickname", joinChalNickname);
		List<JoinChalDTO> joinChalList = memberService.getUserChalList(nickName);
		model.addAttribute("list", joinChalList);;
		return "/user/chaling";
	}

	@RequestMapping("myBoard")
	public String myBoardPage(Model model, String nickName) {
		List<BoardDTO> boardList = memberService.getUserBoard(nickName);
		model.addAttribute("list", boardList);
		return "/user/myBoard";
	}
	@RequestMapping("myBoardReply")
	public String myBoardReply(Model model, String nickName) {
		List<BoardReplyDTO> boardReplyList = memberService.getUserBoardReply(nickName);
		model.addAttribute("list", boardReplyList);
		return "/user/myBoardReply";
	}

	@RequestMapping("delete")
	public String deleteUser(int seq) {
		memberService.delete(seq);
		return "home";
	}

	@RequestMapping("certi") // 인증 상세목록으로 이동.
	public String certi(Model model, int chalSeq, String chalName, String refNickname) {
		// 인증한 목록 출력.
		List<CertiDTO> list = mService.findCertiList(chalSeq, chalName, refNickname);
		model.addAttribute("list",list);
		return "/user/certi";
	}

	@RequestMapping("certiWriteForm") // 인증 작성폼으로 이동.
	public String certiWriteForm(Model model, CertiDTO dto) {
		model.addAttribute("list",dto);
		return "/user/certiwriteform";
	}

	// 인증 작성은 ImageController에서 구현.

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e) {
		e.printStackTrace();
		return "redirect:/";
	}
}