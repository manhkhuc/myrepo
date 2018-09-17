public BufferedImage rotate(BufferedImage image, int degree) {
	    int iw = image.getWidth();
	    int ih = image.getHeight();
	    int w = 0;
	    int h = 0;
	    int x = 0;
	    int y = 0;
	    degree = degree % 360;
	    if (degree < 0)
	        degree = 360 + degree;
	    double ang = degree * 0.0174532925;

	    if (degree == 180 || degree == 0 || degree == 360) {
	        w = iw;
	        h = ih;
	    } else if (degree == 90 || degree == 270) {
	        w = ih;
	        h = iw;
	    } else {
	        int d = iw + ih;
	        w = (int) (d * Math.abs(Math.cos(ang)));
	        h = (int) (d * Math.abs(Math.sin(ang)));
	    }

	    x = (w / 2) - (iw / 2);
	    y = (h / 2) - (ih / 2);
	    BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
	    Graphics gs = rotatedImage.getGraphics();
	    gs.fillRect(0, 0, w, h);
	    AffineTransform at = new AffineTransform();
			
	    at.rotate(ang, w / 2, h / 2);
	    at.translate(x, y);
	    AffineTransformOp op = new AffineTransformOp(at,
	                                                 AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	    op.filter(image, rotatedImage);
	    image = rotatedImage;
	    return image;
	}
    
    private void resize(String processFileName, int resizeBy) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(processFileName));
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        int cropSize = 0;
        int cropH = 0;
        int cropW = 0;
        if (originalImage.getHeight() > originalImage.getWidth()) {
            cropSize = originalImage.getWidth();
            cropH = (originalImage.getHeight() - originalImage.getWidth())/2;
        } else if (originalImage.getHeight() < originalImage.getWidth()){
            cropSize = originalImage.getHeight();
            cropW = (originalImage.getWidth() - originalImage.getHeight())/2;
        }
        BufferedImage cropImage = originalImage.getSubimage(cropW, cropH, cropSize, cropSize);
        BufferedImage resizedImage = new BufferedImage(resizeBy, resizeBy, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(cropImage, 0, 0, resizeBy, resizeBy, null);
        g.dispose();    
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        ImageIO.write(resizedImage, "jpg", new File(processFileName)); 
    }
