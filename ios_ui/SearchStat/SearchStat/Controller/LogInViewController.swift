//
//  LogInViewController.swift
//  SearchStat
//
//  Created by Евгений Скилиоти on 19.12.2017.
//  Copyright © 2017 Евгений Скилиоти. All rights reserved.
//

import UIKit

class LogInViewController: UIViewController {
    
    @IBOutlet weak var emailTextField: CustomTextField!
    @IBOutlet weak var passwordTextField: CustomTextField!
    
    @IBAction func restorePasswordBtn(_ sender: UIButton) {
    }
    
    @IBAction func logInBtn(_ sender: CustomButton) {
        //authorization...
        guard let email = emailTextField.text , emailTextField.text != "" else {return }
        guard let pass = passwordTextField.text, passwordTextField.text != "" else { return }
        
        
        AuthService.instance.loginUser(email: email, password: pass) { (success) in
            if success {
                UserDataService.instance.setUserData(id: "0", email: email, name: "Admin")
                self.performSegue(withIdentifier: UNWIND, sender: nil)
                NotificationCenter.default.post(name: NOTIF_USER_DID_CHANGED, object: nil)
                
            }
        }
        
        
    }
        
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
        
    }
    
    func setupView() {
        let tap = UITapGestureRecognizer(target: self, action: #selector(RegistrationViewController.handleTap))
        view.addGestureRecognizer(tap)
    }
    
    @objc func handleTap() {
        view.endEditing(true)
    }


}
