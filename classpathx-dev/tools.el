;;some emacs lisp utilities for dealing with classpathx code
;;(c) Nic Ferrier 2001


;;non-Nic user's will have to talk to Nic to get tf-find
(require 'tf-find)

;;use tf-find to modify all .java files to the GNU coding std.
(defun gnuify (sourcecode-path)
  (interactive "Djava files source code path: ")
  (tf-find
   (list sourcecode-path)

   (lambda (file)
     (tf-type-matcher file "java"))

   (lambda (filename)
     ;;visit the file and search for the string
     (let ((buf (find-file filename)))
       (condition-case nil
	   (save-current-buffer
	     (set-buffer buf)
	     (massage-gnubraces)
	     (massage-badtabs)
	     (massage-badlines)
	     (beginning-of-buffer)
	     (search-forward "{")
	     (search-forward " ")
	     (c-indent-defun)
	     (save-buffer)
	     )
	 (error nil)
	 )
       (kill-buffer buf)
       )
     )
   )
  )

;;use tf-find to replace the (c) message with one found in buffer "copyright"
(defun classpathx-copyright (sourcecode-path)
  (interactive "Djava files source code path")
  (let ((copyright-buffer (get-buffer "copyright")))
    (tf-find
     (list sourcecode-path)
     (lambda (file)
       (tf-type-matcher file "java"))
     (lambda (filename)
       (let ((tbuf (find-file-noselect filename)))
	 (condition-case nil
	     (save-current-buffer
	       (set-buffer tbuf)
	       (beginning-of-buffer)
	       ;;find the package statement
	       (let ((found (re-search-forward "^package .*;$")))
		 ;;delete the text before the package name
		 (delete-region (point-min) (match-beginning 0))
		 ;;insert the new text
		 (beginning-of-buffer)
		 (insert-buffer buffer)
		 (save-buffer)
		 )
	       )
	   (error nil)
	   )
	 (kill-buffer tbuf)
	 )
       )
     )
    )
  )



;;some code massage procs

;;removes multiple tabs, replacing them with spaces
(defun massage-badtabs ()
  "use a regexp to replace all tabs with spaces.
Muliple tabs are replaced with a single space." 
  (interactive)
  (save-excursion
    (let ((pos (re-search-forward "\\(\t+\\)[^\t]" nil 't)))
      (while pos
	(replace-match " " 't nil nil 1)
	(setq pos (re-search-forward  "\\(\t+\\)[^\t]" nil 't))))))

;;removes multiple tabs, replacing them with spaces
(defun massage-badlines ()
  "use a regexp to replace all the lines that are blank but
not empty with lines that are blank and empty." 
  (interactive)
  (save-excursion
    (let ((pos (re-search-forward "^\\([ \t]\\)$" nil 't)))
      (while pos
	(replace-match "" 't nil nil 1)
	(setq pos (re-search-forward "^\\([ \t]\\)$" nil 't))))))

;;changes K&R style to GNU style braces
(defun massage-gnubraces ()
  "use a regexp to gnu-ify K&R brace c-code." 
  (interactive)
  (save-excursion
    (let ((pos (re-search-forward "[^ \t\n][ \t]*\\({\\)$" nil 't)))
      (while pos
	(replace-match "\n{" 't nil nil 1)
	(setq pos (re-search-forward  "[^ \t\n][ \t]*\\({\\)$" nil 't))))))
