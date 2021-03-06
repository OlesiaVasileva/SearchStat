//
//  SourceCell.swift
//  SearchStat
//
//  Created by Roman Trekhlebov on 21.12.2017.
//  Copyright © 2017 Евгений Скилиоти. All rights reserved.
//

import UIKit

class SourceCell: UITableViewCell {

    @IBOutlet weak var siteLbl: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        siteLbl.layer.borderColor = UIColor.lightGray.cgColor
        siteLbl.layer.borderWidth = 1

        
    }
    
    func setupCell(site: SiteModel) {
        siteLbl.text = site.name
    }

}
